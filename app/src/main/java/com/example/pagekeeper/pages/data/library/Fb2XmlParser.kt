package com.example.pagekeeper.pages.data.library

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Xml
import com.example.pagekeeper.core.domain.util.ParserError
import com.example.pagekeeper.core.domain.util.Result
import com.example.pagekeeper.pages.domain.library.Book
import com.example.pagekeeper.pages.domain.library.LibraryStorage
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.library.XmlParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import timber.log.Timber
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

class Fb2XmlParser(
    private val libraryStorage: LibraryStorage,
    private val pageDataSource: PageDataSource
) : XmlParser {
    private data class Metadata(
        val title: String,
        val authors: List<String>,
        val documentId: String,
        val coverPage: String
    )

    private data class TitleInfo(
        val title: String,
        val authors: List<String>,
        val coverPage: String,
    )

    override suspend fun parseBook(uri: Uri): Result<Book, ParserError> {
        return withContext(Dispatchers.Default) {
            try {
                val tempFilePath =
                    libraryStorage.saveBookTemporarily(uri) ?: return@withContext Result.Error(
                        ParserError.IO_ERROR
                    )
                val bookUUID = "book-" + UUID.randomUUID().toString()
                val documentIds = pageDataSource.observeDocumentsId().firstOrNull() ?: emptyList()
                File(tempFilePath).inputStream().buffered().use { bufferedStream ->
                    val parser = Xml.newPullParser().apply {
                        setInput(bufferedStream, null)
                    }

                    parser.nextTag()
                    parseBook(parser)
                }?.let { book ->
                    if (documentIds.any { it == book.documentId })
                        return@withContext Result.Error(ParserError.DUPLICATE_ERROR)

                    val parentPath = libraryStorage.createDirectory(bookUUID) ?: return@let null
                    val timestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString()
                    val bookFileName = "${book.title}.${LibraryStorage.BOOK_EXTENSION}"

                    libraryStorage.savePersistently(tempFilePath, parentPath, bookFileName)?.let { bookFilePath ->
                        var imageFilePath: String? = null

                        book.coverPath?.let { tempPath ->
                            val imageFileName = "${LibraryStorage.PERSISTENT_IMAGE_PREFIX}-$timestamp.${LibraryStorage.IMAGE_EXTENSION}"
                            imageFilePath = libraryStorage.savePersistently(tempPath, parentPath, imageFileName)
                        }

                        val newBook = book.copy(bookPath = bookFilePath, coverPath = imageFilePath)
                        libraryStorage.cleanUpTemporaryFiles()

                        Result.Success(newBook)
                    } ?: Result.Error(ParserError.IO_ERROR)

                } ?: Result.Error(ParserError.PARSING_ERROR)
            } catch (e: XmlPullParserException) {
                Timber.e("Error parsing FB2 file: ${e.message}")
                Result.Error(ParserError.PARSING_ERROR)
            } catch (e: Exception) {
                ensureActive()
                Timber.e("Other error: ${e.message}")
                Result.Error(ParserError.UNKNOWN_ERROR)
            }
        }
    }

    override suspend fun deleteBook(path: String): Boolean? {
        return withContext(Dispatchers.IO) {
            val file = File(path)
            if (file.exists()) {
                file.parentFile?.deleteRecursively()
            } else
                null
        }
    }

    private suspend fun parseBook(
        parser: XmlPullParser
    ): Book? {
        var metadata: Metadata? = null
        var coverPath: String? = null

        parser.require(XmlPullParser.START_TAG, null, "FictionBook")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "description" -> metadata = parseDescription(parser)
                "binary" -> coverPath = parseBinary(parser, metadata?.coverPage)
                else -> skip(parser)
            }
        }

        return metadata?.let { metadata ->
            Book(
                title = metadata.title,
                author = metadata.authors.filter { it.isNotBlank() }.joinToString(", "),
                addedAt = Instant.now(),
                coverPath = coverPath,
                bookPath = null,
                documentId = metadata.documentId
            )
        }
    }

    private fun parseDescription(parser: XmlPullParser): Metadata {
        var title = ""
        val authors = mutableListOf<String>()
        var documentId = ""
        var coverPage = ""

        parser.require(XmlPullParser.START_TAG, null, "description")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "title-info" -> {
                    val info = parseTitleInfo(parser)
                    title = info.title
                    coverPage = info.coverPage
                    authors.addAll(info.authors)
                }

                "document-info" -> documentId = parseDocumentInfo(parser)
                else -> skip(parser)
            }
        }
        return Metadata(title, authors, documentId, coverPage)
    }

    private fun parseTitleInfo(parser: XmlPullParser): TitleInfo {
        var title = ""
        val authors = mutableListOf<String>()
        var coverPage = ""

        parser.require(XmlPullParser.START_TAG, null, "title-info")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "book-title" -> title = parser.nextText()
                "author" -> authors.add(parseAuthor(parser))
                "coverpage" -> coverPage = parseCover(parser)
                else -> skip(parser)
            }
        }
        return TitleInfo(title, authors, coverPage)
    }

    private fun parseAuthor(parser: XmlPullParser): String {
        var firstName = ""
        var lastName = ""
        var middleName = ""

        parser.require(XmlPullParser.START_TAG, null, "author")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "first-name" -> firstName = parser.nextText()
                "last-name" -> lastName = parser.nextText()
                "middle-name" -> middleName = parser.nextText()
                else -> skip(parser)
            }
        }
        return listOf(firstName, middleName, lastName)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }

    private fun parseCover(parser: XmlPullParser): String {
        var href = ""
        parser.require(XmlPullParser.START_TAG, null, "coverpage")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "image" -> {
                    href = parser.getAttributeValue(null, "href")
                        .removePrefix("#")
                    parser.nextTag()
                }

                else -> skip(parser)
            }
        }
        return href
    }

    private fun parseDocumentInfo(parser: XmlPullParser): String {
        var id = ""
        parser.require(XmlPullParser.START_TAG, null, "document-info")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "id" -> id = parser.nextText()
                else -> skip(parser)
            }
        }
        return id
    }

    private suspend fun parseBinary(
        parser: XmlPullParser,
        coverPage: String?
    ): String? {
        parser.require(XmlPullParser.START_TAG, null, "binary")
        val id = parser.getAttributeValue(null, "id")
        var imagePath: String? = null

        if (id == coverPage) {
            val base64String = parser.nextText().trim()
            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imagePath = libraryStorage.saveBookImageTemporarily(bitmap)
        } else
            skip(parser)

        return imagePath
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}
