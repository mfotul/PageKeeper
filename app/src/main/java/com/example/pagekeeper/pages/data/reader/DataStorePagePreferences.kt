package com.example.pagekeeper.pages.data.reader

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pagekeeper.pages.domain.library.PagePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DataStorePagePreferences(
    private val context: Context
) : PagePreferences {
    private val Context.pageDataStore by preferencesDataStore(
        name = "page_datastore"
    )

    private val fontSizeKey = floatPreferencesKey("font_size")
    private val recentlyOpenedBooksKey = stringPreferencesKey("recently_opened_books")

    override suspend fun saveFontSize(fontSize: Float) {
        context.pageDataStore.edit { prefs ->
            prefs[fontSizeKey] = fontSize
        }
    }

    override suspend fun saveRecentlyOpenedBooks(books: List<Long>) {
        context.pageDataStore.edit { prefs ->
            prefs[recentlyOpenedBooksKey] = books.joinToString(",")
        }
    }

    override fun observerFontSize(): Flow<Float> {
        return context
            .pageDataStore
            .data
            .map { prefs ->
                prefs[fontSizeKey] ?: 18f
            }
            .distinctUntilChanged()
    }

    override fun observeRecentlyOpenedBooks(): Flow<List<Long>> {
        return context
            .pageDataStore
            .data
            .map { prefs ->
                val serializedList = prefs[recentlyOpenedBooksKey] ?: ""
                if (serializedList.isEmpty())
                    emptyList()
                else
                    serializedList
                        .split(",")
                        .mapNotNull { it.toLongOrNull() }
            }
            .distinctUntilChanged()
    }
}