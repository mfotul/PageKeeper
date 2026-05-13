package com.example.pagekeeper.pages.presentation.navigation

sealed interface NavigationAction {
    data object OnBackClick : NavigationAction
    data class OnChapterClick(val elementId: Long) : NavigationAction
}