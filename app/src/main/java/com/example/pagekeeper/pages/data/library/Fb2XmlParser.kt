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
import com.example.pagekeeper.pages.domain.reader.Element
import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.domain.reader.StyledText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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
    private val pageDataSource: PageDataSource,
    private val applicationScope: CoroutineScope
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
        return withContext(Dispatchers.IO) {
            try {
                val tempFilePath =
                    libraryStorage.saveBookTemporarily(uri) ?: return@withContext Result.Error(
                        ParserError.IO_ERROR
                    )
                val bookUUID = LibraryStorage.PERSISTENT_BOOK_PREFIX + "-" + UUID.randomUUID().toString()
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

                    libraryStorage.savePersistently(tempFilePath, parentPath, bookFileName)
                        ?.let { bookFilePath ->
                            var imageFilePath: String? = null

                            book.coverPath?.let { tempPath ->
                                val imageFileName =
                                    "${LibraryStorage.PERSISTENT_IMAGE_PREFIX}-$timestamp.${LibraryStorage.IMAGE_EXTENSION}"
                                imageFilePath = libraryStorage.savePersistently(
                                    tempPath,
                                    parentPath,
                                    imageFileName
                                )
                            }

                            val newBook =
                                book.copy(bookPath = bookFilePath, coverPath = imageFilePath)
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

    override suspend fun parseBookBodyFile(bookId: Long): Result<Unit, ParserError> {
        return withContext(Dispatchers.IO) {
            try {
                val book = pageDataSource.observeBookById(bookId).firstOrNull()
                    ?: return@withContext Result.Error(ParserError.IO_ERROR)

                if (book.bookPath == null) return@withContext Result.Error(ParserError.IO_ERROR)

                val bookFile = File(book.bookPath)
                if (!bookFile.exists()) return@withContext Result.Error(ParserError.IO_ERROR)

                applicationScope.launch {
                    bookFile.inputStream().buffered().use { bufferedStream ->
                        val parser = Xml.newPullParser().apply {
                            setInput(bufferedStream, null)
                        }

                        parser.next()
                        parseBookBody(parser, book.bookId!!)
                    }
                }
                Result.Success(Unit)
            } catch (e: XmlPullParserException) {
                Timber.e("Error parsing FB2 file: ${e.message}")
                Result.Error(ParserError.PARSING_ERROR)
            } catch (e: Exception) {
                ensureActive()
                Timber.e(e)
                Result.Error(ParserError.UNKNOWN_ERROR)
            }
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
                documentId = metadata.documentId,
                readingPositionIndex = 0,
                readingPositionOffset = 0,
                readingProgress = 0f
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

    private suspend fun parseBookBody(parser: XmlPullParser, bookId: Long) {
        parser.require(XmlPullParser.START_TAG, null, "FictionBook")
        var bodyNumber = 1

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "body" -> parseBody(parser, bookId, bodyNumber++)
                else -> skip(parser)
            }
        }
    }

    private suspend fun parseBody(
        parser: XmlPullParser,
        bookId: Long,
        bodyId: Int
    ) {
        parser.require(XmlPullParser.START_TAG, null, "body")
        var sectionNumber = 1

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "section" -> parseSection(
                    parser = parser,
                    bookId = bookId,
                    bodyId = bodyId,
                    sectionId = sectionNumber++
                )
                "title" -> {
                    val element = Element(
                        bookId = bookId,
                        bodyId = bodyId,
                        sectionId = 0,
                        content = parseTitle(parser)
                    )
                    pageDataSource.insertElement(element)
                }
                else -> skip(parser)
            }
        }
    }

    private suspend fun parseSection(
        parser: XmlPullParser,
        bookId: Long,
        bodyId: Int,
        sectionId: Int
    ) {
        parser.require(XmlPullParser.START_TAG, null, "section")
        var content: Fb2BlockElement? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "p" -> content = Fb2BlockElement.Paragraph(parseParagraph(parser, "p"))
                "subtitle" -> content = Fb2BlockElement.Subtitle(
                    parseParagraph(
                        parser,
                        "subtitle"
                    )
                )

                "title" -> content = parseTitle(parser)
                "empty-line" -> content = parseEmptyLine(parser)
                "epigraph" -> content = parseEpigraph(parser, "epigraph")
                "cite" -> content = parseEpigraph(parser, "cite")
                else -> skip(parser)
            }

            content?.let { content ->
                val element = Element(
                    bookId = bookId,
                    content = content,
                    bodyId = bodyId,
                    sectionId = sectionId
                )
                pageDataSource.insertElement(element)
            }
            content = null
        }
    }

    private fun parseTitle(parser: XmlPullParser): Fb2BlockElement {
        parser.require(XmlPullParser.START_TAG, null, "title")
        val title = mutableListOf<List<StyledText>>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "p" -> title.add(parseParagraph(parser, "p"))
                else -> skip(parser)
            }
        }
        return Fb2BlockElement.Title(title)
    }

    private fun parseEpigraph(parser: XmlPullParser, name: String): Fb2BlockElement {
        parser.require(XmlPullParser.START_TAG, null, name)

        val lines = mutableListOf<List<StyledText>>()
        var author: List<StyledText>? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "p" -> lines.add(parseParagraph(parser, "p"))
                "text-author" -> author = parseParagraph(parser, "text-author")
            }
        }
        return Fb2BlockElement.Cite(lines = lines, author = author)
    }

    private fun parseParagraph(parser: XmlPullParser, name: String): List<StyledText> {
        parser.require(XmlPullParser.START_TAG, null, name)

        val result = mutableListOf<StyledText>()
        var isBold = false
        var isItalic = false

        while (!(parser.next() == XmlPullParser.END_TAG && parser.name == name)) {
            when (parser.eventType) {
                XmlPullParser.TEXT -> result.add(
                    StyledText(
                        text = parser.text,
                        isBold = isBold,
                        isItalic = isItalic
                    )
                )

                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "strong" -> isBold = true
                        "emphasis" -> isItalic = true
                    }
                }

                XmlPullParser.END_TAG -> {
                    when (parser.name) {
                        "strong" -> isBold = false
                        "emphasis" -> isItalic = false
                    }
                }
            }
        }
        return result
    }

    private fun parseEmptyLine(parser: XmlPullParser): Fb2BlockElement {
        parser.require(XmlPullParser.START_TAG, null, "empty-line")
        parser.nextTag()
        return Fb2BlockElement.EmptyLine
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
