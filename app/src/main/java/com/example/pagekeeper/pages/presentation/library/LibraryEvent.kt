package com.example.pagekeeper.pages.presentation.library

sealed interface LibraryEvent {
    data object OnDrawerOpen : LibraryEvent
    data object OnDrawerClose : LibraryEvent
    data object OnImportBook: LibraryEvent
    data class OnShareBook(val path: String): LibraryEvent
    data class OnShareMultipleBooks(val paths: List<String>): LibraryEvent
}