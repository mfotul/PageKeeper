package com.example.pagekeeper.core.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.R

val Inter = FontFamily(
    Font(
        resId = R.font.inter_variable,
        weight = FontWeight.Normal,
    ),
    Font(
        resId = R.font.inter_variable,
        weight = FontWeight.Medium,
    )
)

val Lora = FontFamily(
    Font(
        resId = R.font.lora_variable,
        weight = FontWeight.Bold,
    ),
    Font(
        resId = R.font.lora_variable,
        weight = FontWeight.Medium,
    ),
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Lora,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),


)