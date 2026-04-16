package com.example.pagekeeper.pages.data.library

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.pagekeeper.pages.domain.library.LibraryStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.UUID

class InternalLibraryStorage(
    private val context: Context
) : LibraryStorage {
    override suspend fun savePersistently(
        tempFilePath: String,
        parentPath: String,
        fileName: String
    ): String? {
        val tempFile = File(tempFilePath)
        if (!tempFile.exists()) {
            return null
        }

        return withContext(Dispatchers.IO) {
            try {
                val savedFile = File(parentPath, fileName)
                tempFile.copyTo(savedFile)
                savedFile.absolutePath
            } catch (e: IOException) {
                Timber.e(e)
                null
            }
        }
    }

    override suspend fun saveBookTemporarily(uri: Uri): String? {
        val destinationFile = generateTempFile(LibraryStorage.TEMP_BOOK_PREFIX)
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    destinationFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                destinationFile.absolutePath
            } catch (e: IOException) {
                Timber.e(e)
                null
            }
        }
    }

    override suspend fun saveBookImageTemporarily(bitmap: Bitmap): String? {
        val destinationFile = generateTempFile(LibraryStorage.TEMP_IMAGE_PREFIX)
        return withContext(Dispatchers.IO) {
            try {
                destinationFile.outputStream().use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }
                destinationFile.absolutePath
            } catch (e: IOException) {
                Timber.e(e)
                null
            }
        }
    }

    override suspend fun cleanUpTemporaryFiles() {
        withContext(Dispatchers.IO) {
            context
                .cacheDir
                .listFiles()
                ?.filter {
                    it.name.startsWith(LibraryStorage.TEMP_BOOK_PREFIX)
                            || it.name.startsWith(LibraryStorage.TEMP_IMAGE_PREFIX)
                }
                ?.forEach { file ->
                    file.delete()
                }
        }
    }

    override suspend fun createDirectory(bookUUID: String): String? {
        val bookDir = context.filesDir.resolve(bookUUID)
        return withContext(Dispatchers.IO) {
            try {
                if (!bookDir.exists()) {
                    bookDir.mkdirs()
                }
                bookDir.absolutePath
            } catch (e: IOException) {
                Timber.e(e, "Unable create directory: $bookDir")
                null
            }
        }
    }

    private fun generateTempFile(filePrefix: String): File {
        val id = UUID.randomUUID().toString()
        return File(
            context.cacheDir,
            "${filePrefix}_$id.${LibraryStorage.BOOK_EXTENSION}"
        )
    }
}