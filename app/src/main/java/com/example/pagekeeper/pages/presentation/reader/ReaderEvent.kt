package com.example.pagekeeper.pages.presentation.reader

sealed interface ReaderEvent {
    data object OnDeviceScreenLock: ReaderEvent
}