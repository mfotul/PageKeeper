package com.example.pagekeeper.pages.presentation.navigation

import androidx.compose.runtime.Stable
import com.example.pagekeeper.pages.presentation.navigation.models.ContentUi

@Stable
data class NavigationState(
    val contents: List<ContentUi> = emptyList()
)
