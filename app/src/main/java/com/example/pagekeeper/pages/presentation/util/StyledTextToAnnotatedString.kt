package com.example.pagekeeper.pages.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.core.presentation.designsystem.theme.Inter
import com.example.pagekeeper.pages.domain.reader.StyledText


fun List<StyledText>.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        withStyle(SpanStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
        )) {
            forEach { span ->
                withStyle(
                    style = SpanStyle(
                        fontWeight = if (span.isBold) FontWeight.SemiBold else FontWeight.Normal,
                        fontStyle = if (span.isItalic) FontStyle.Italic else FontStyle.Normal
                    )
                ) {
                    append(span.text)
                }
            }
        }
    }
}