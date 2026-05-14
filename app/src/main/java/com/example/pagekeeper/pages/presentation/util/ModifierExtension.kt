package com.example.pagekeeper.pages.presentation.util

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(
    condition: Boolean,
    factory: Modifier.() -> Modifier
): Modifier = if (condition) factory() else this