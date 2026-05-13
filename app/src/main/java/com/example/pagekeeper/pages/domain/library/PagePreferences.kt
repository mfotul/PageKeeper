package com.example.pagekeeper.pages.domain.library

import kotlinx.coroutines.flow.Flow

interface PagePreferences {
    suspend fun saveFontSize(fontSize: Float)
    suspend fun saveRecentlyOpenedBooks(books: List<Long>)
    fun observerFontSize(): Flow<Float>
    fun observeRecentlyOpenedBooks(): Flow<List<Long>>
}