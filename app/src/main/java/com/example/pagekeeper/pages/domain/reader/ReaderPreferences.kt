package com.example.pagekeeper.pages.domain.reader

import kotlinx.coroutines.flow.Flow

interface ReaderPreferences {
    suspend fun saveFontSize(fontSize: Float)
    fun observerFontSize(): Flow<Float>
}