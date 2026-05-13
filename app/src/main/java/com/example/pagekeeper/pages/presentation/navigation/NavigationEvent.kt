package com.example.pagekeeper.pages.presentation.navigation

sealed interface NavigationEvent {
    data class OnChapterSelected(val index: Int) : NavigationEvent
}
