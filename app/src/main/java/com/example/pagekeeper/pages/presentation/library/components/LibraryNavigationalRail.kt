package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.button.PrimaryButton
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.tabletBlockBackground
import com.example.pagekeeper.pages.presentation.library.models.Screen
import com.example.pagekeeper.pages.presentation.library.LibraryAction
import com.example.pagekeeper.pages.presentation.util.getIconForScreen
import com.example.pagekeeper.pages.presentation.util.getStringForScreen
import kotlinx.coroutines.launch

@Composable
fun LibraryNavigationalRail(
    selectedScreen: Screen,
    onAction: (LibraryAction) -> Unit,
    isEnabled: Boolean,
    recentlyOpenedBookCard: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (isEnabled) {
        val railState =
            rememberWideNavigationRailState(initialValue = WideNavigationRailValue.Expanded)
        val scope = rememberCoroutineScope()
        var drawerItemWidth by remember { mutableIntStateOf(Int.MAX_VALUE) }

        Row(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = 64.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
        ) {
            WideNavigationRail(
                state = railState,
                colors = WideNavigationRailDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                modifier = Modifier
                    .widthIn(min = 112.dp, max = 260.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = if (railState.targetValue == WideNavigationRailValue.Expanded)
                        Alignment.Start
                    else
                        Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (railState.targetValue == WideNavigationRailValue.Expanded)
                                    railState.collapse()
                                else
                                    railState.expand()
                            }
                        },
                    ) {
                        Icon(
                            painter = painterResource(
                                if (railState.targetValue == WideNavigationRailValue.Expanded)
                                    R.drawable.baseline_menu_open_24
                                else
                                    R.drawable.baseline_menu_24
                            ),
                            contentDescription = stringResource(R.string.close_menu)
                        )
                    }
                    PrimaryButton(
                        text = stringResource(R.string.import_book),
                        onClick = { onAction(LibraryAction.OnImportBookClick) },
                        iconRes = R.drawable.import_book,
                        letterSpacing = (-0.15).sp,
                        isCollapsed = railState.targetValue == WideNavigationRailValue.Collapsed,
                        modifier = Modifier
                            .onSizeChanged {
                                drawerItemWidth = it.width
                            }
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Screen.entries.forEach { screen ->
                        val isActive = selectedScreen == screen
                        LibraryNavigationRailItem(
                            railExpanded = railState.targetValue == WideNavigationRailValue.Expanded,
                            label = {
                                Text(
                                    text = stringResource(getStringForScreen(screen))
                                )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        getIconForScreen(
                                            screen,
                                            isActive
                                        )
                                    ),
                                    contentDescription = null
                                )
                            },
                            selected = isActive,
                            onClick = { onAction(LibraryAction.OnScreenChange(screen)) },
                            modifier = Modifier
                                .width(
                                    max(
                                        128.dp,
                                        with(LocalDensity.current) { drawerItemWidth.toDp() }
                                    )
                                )
                                .height(62.dp)
                        )

                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                recentlyOpenedBookCard()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.tabletBlockBackground,
                            shape = RoundedCornerShape(28.dp)
                        )
                ) {
                    content()
                }
            }
        }
    } else
        Column {
            content()
        }
}

@Preview(device = TABLET)
@Composable
private fun LibraryNavigationalRailPreview() {
    PageKeeperTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LibraryNavigationalRail(
                selectedScreen = Screen.BOOKMARKS,
                isEnabled = true,
                recentlyOpenedBookCard = {

                },
                onAction = {}
            ) {

            }
        }
    }
}