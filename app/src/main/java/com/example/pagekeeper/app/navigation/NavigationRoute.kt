package com.example.pagekeeper.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavigationRoute: NavKey {
    @Serializable
    data object LibraryScreen: NavigationRoute, NavKey

    @Serializable
    data class ReaderScreen(val bookId: Int): NavigationRoute, NavKey
}