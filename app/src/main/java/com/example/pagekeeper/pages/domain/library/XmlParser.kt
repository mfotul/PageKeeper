package com.example.pagekeeper.pages.domain.library

import android.net.Uri
import com.example.pagekeeper.core.domain.util.ParserError
import com.example.pagekeeper.core.domain.util.Result

interface XmlParser {
    suspend fun parseBook(uri: Uri): Result<Book, ParserError>
    suspend fun parseBookBodyFile(bookId: Int): Result<Unit, ParserError>
    suspend fun deleteBook(path: String): Boolean?
}