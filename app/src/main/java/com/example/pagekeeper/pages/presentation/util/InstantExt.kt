package com.example.pagekeeper.pages.presentation.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Instant.toReadableTime(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm MMM d, yyyy", Locale.US)
    return this.atZone(ZoneId.systemDefault()).format(formatter)
}