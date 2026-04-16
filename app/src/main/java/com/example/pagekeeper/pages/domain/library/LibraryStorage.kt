package com.example.pagekeeper.pages.domain.library

import android.graphics.Bitmap
import android.net.Uri

interface LibraryStorage {
    suspend fun savePersistently(tempFilePath: String, parentPath: String, fileName: String): String?
    suspend fun saveBookTemporarily(uri: Uri): String?
    suspend fun saveBookImageTemporarily(bitmap: Bitmap): String?
    suspend fun cleanUpTemporaryFiles()
    suspend fun createDirectory(bookUUID: String): String?

    companion object {
        const val BOOK_EXTENSION = "fb2"
        const val IMAGE_EXTENSION = "jpg"
        const val TEMP_BOOK_PREFIX = "temp_book"
        const val PERSISTENT_BOOK_PREFIX = "book"
        const val PERSISTENT_IMAGE_PREFIX = "image"
        const val TEMP_IMAGE_PREFIX = "temp_image"
    }
}