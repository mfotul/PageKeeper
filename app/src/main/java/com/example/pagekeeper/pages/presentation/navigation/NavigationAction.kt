package com.example.pagekeeper.pages.presentation.navigation

import com.example.pagekeeper.pages.presentation.navigation.models.ContentUi

sealed interface NavigationAction {
    data object OnBackClick : NavigationAction
    data class OnChapterClick(val elementId: Long) : NavigationAction
    data class OnTitleClick(val contentUi: ContentUi) : NavigationAction
}