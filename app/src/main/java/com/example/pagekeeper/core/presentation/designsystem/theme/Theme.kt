package com.example.pagekeeper.core.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimaryText,
    onSecondary = OnSecondaryText,
    background = Background,
    surface = CardSurface,
    onError = StateAlert,
    onSurface = ActiveSurface,
    outline = Divider,
)

@Composable
fun PageKeeperTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}