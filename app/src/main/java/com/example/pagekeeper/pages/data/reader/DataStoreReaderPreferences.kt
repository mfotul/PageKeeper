package com.example.pagekeeper.pages.data.reader

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pagekeeper.pages.domain.reader.ReaderPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DataStoreReaderPreferences(
    private val context: Context
): ReaderPreferences {
    private val Context.readerDataStore by preferencesDataStore(
        name = "reader_datastore"
    )

    private val fontSizeKey = floatPreferencesKey("font_size")

    override suspend fun saveFontSize(fontSize: Float) {
        context.readerDataStore.edit { prefs ->
            prefs[fontSizeKey] = fontSize
        }
    }

    override fun observerFontSize(): Flow<Float> {
        return context
            .readerDataStore
            .data
            .map { prefs ->
                prefs[fontSizeKey] ?: 18f
            }
            .distinctUntilChanged()
    }


}