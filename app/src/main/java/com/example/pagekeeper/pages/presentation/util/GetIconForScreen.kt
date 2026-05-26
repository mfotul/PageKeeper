package com.example.pagekeeper.pages.presentation.util

import com.example.pagekeeper.R
import com.example.pagekeeper.pages.presentation.library.models.Screen

fun getIconForScreen(screen: Screen, isActive: Boolean): Int {
    return if (isActive)
        when (screen) {
            Screen.LIBRARY -> R.drawable.menu_library_active
            Screen.FAVORITES -> R.drawable.menu_favorites_active
            Screen.FINISHED -> R.drawable.menu_finished_active
            Screen.BOOKMARKS -> R.drawable.bookmark_color
        }
    else
        when (screen) {
            Screen.LIBRARY -> R.drawable.menu_library_deactive
            Screen.FAVORITES -> R.drawable.favorites
            Screen.FINISHED -> R.drawable.menu_finished_deactive
            Screen.BOOKMARKS -> R.drawable.bookmark
        }
}