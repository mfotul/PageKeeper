package com.example.pagekeeper.pages.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.core.presentation.designsystem.theme.Inter
import com.example.pagekeeper.pages.domain.reader.StyledText


fun List<StyledText>.toAnnotatedString(
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    lineHeightMultiplier: Float = 1.33f
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontFamily = Inter,
                fontWeight = fontWeight,
                fontSize = fontSize,
                fontStyle = fontStyle,
            )
        ) {
            withStyle(style = ParagraphStyle(lineHeight = fontSize * lineHeightMultiplier)) {
                forEach { span ->
                    withStyle(
                        style = SpanStyle(
                            fontWeight = if (span.isBold) FontWeight.SemiBold else fontWeight,
                            fontStyle = if (span.isItalic) FontStyle.Italic else fontStyle,
                        )
                    ) {
                        append(span.text)
                    }
                }
            }
        }
    }
}