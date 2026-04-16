package com.example.pagekeeper.core.presentation.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

val OnPrimaryText = Color(0xFF2C2926)
val OnSecondaryText = Color(0xFF706C61)
val Background = Color(0xFFFDFCF8)
val CardSurface = Color(0xFFE4CDA8)
val ActiveSurface = Color(0xFFF1EBDF)
val Primary = Color(0xFF8C7851)
val Icons = Color(0xFF706C61)
val Divider = Color(0xFFE1DDD0)
val StateFinished = Color(0xFF14AF62)
val StateAlert = Color(0xFFDC362E)
val TabletBlockBackground = Color(0xFFF1EBDF)

val ColorScheme.icons: Color
    get() = Icons

val ColorScheme.stateFinished: Color
    get() = StateFinished

val ColorScheme.tabletBlockBackground: Color
    get() = TabletBlockBackground