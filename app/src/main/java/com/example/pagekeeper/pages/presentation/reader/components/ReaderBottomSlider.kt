@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pagekeeper.pages.presentation.reader.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.bgBottomNav
import com.example.pagekeeper.core.presentation.designsystem.theme.loaderSecondary

@Composable
fun ReaderBottomSlider(
    fontSize: Float,
    onFontSizeSet: (Float) -> Unit,
    onSliderPositionChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val primaryColor = MaterialTheme.colorScheme.primary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .background(MaterialTheme.colorScheme.bgBottomNav)
            .padding(16.dp)
            .padding(bottom = 16.dp)
    ) {
        ChangeQuantity(
            onClick = {
                onFontSizeSet(fontSize - 1)
            },
            iconRes = R.drawable.minus,
            iconDescription = R.string.decrease_font_size
        )
        Slider(
            value = fontSize,
            onValueChange = {
                onSliderPositionChange(it)
            },
            onValueChangeFinished = {
                onFontSizeSet(fontSize)
            },
            valueRange = 10f..40f,
            interactionSource = interactionSource,
            thumb = {
                Label(
                    label = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    color = Color.White,
                                    shape = CircleShape
                                )
                        ) {
                            Text("%.0f".format(it.value))
                        }
                    },
                    interactionSource = interactionSource,
                ) {
                    Canvas(
                        modifier = Modifier
                            .width(4.dp)
                            .height(44.dp)
                    ) {
                        drawRoundRect(
                            color = primaryColor,
                            cornerRadius = CornerRadius(x = 4.dp.toPx(), y = 4.dp.toPx())
                        )
                    }
                }
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    drawStopIndicator = { offset ->
                        drawCircle(
                            center = offset,
                            color = primaryColor,
                            radius = 2.dp.toPx(),
                            blendMode = BlendMode.SrcOver
                        )
                    },
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.loaderSecondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.loaderSecondary,
                        activeTickColor = MaterialTheme.colorScheme.primary,
                        inactiveTickColor = MaterialTheme.colorScheme.primary
                    ),
                )
            },
            modifier = Modifier
                .weight(1f)
        )
        ChangeQuantity(
            onClick = {
                onFontSizeSet(fontSize + 1)
            },
            iconRes = R.drawable.plus,
            iconDescription = R.string.increase_font_size
        )
    }
}

@Composable
fun ChangeQuantity(
    onClick: () -> Unit,
    @DrawableRes iconRes: Int,
    @StringRes iconDescription: Int,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(40.dp)
                .height(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(100.dp)
                )
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = stringResource(iconDescription),
                tint = MaterialTheme.colorScheme.background,
            )
        }
    }
}

@Preview
@Composable
private fun ReaderBottomSliderPreview() {
    PageKeeperTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ReaderBottomSlider(
                fontSize = 18f,
                onFontSizeSet = {},
                onSliderPositionChange = {}
            )
        }
    }
}