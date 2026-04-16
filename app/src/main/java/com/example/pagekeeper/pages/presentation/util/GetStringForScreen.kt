package com.example.pagekeeper.pages.presentation.util

import com.example.pagekeeper.R
import com.example.pagekeeper.pages.presentation.library.models.Screen

fun getStringForScreen(screen: Screen): Int {
    return when (screen) {
        Screen.LIBRARY -> R.string.library_label
        Screen.FAVORITES -> R.string.favorites_label
        Screen.FINISHED -> R.string.finished_label
    }
}
