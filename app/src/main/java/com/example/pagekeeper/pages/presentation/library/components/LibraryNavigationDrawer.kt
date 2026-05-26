package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.button.PrimaryButton
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.icons
import com.example.pagekeeper.pages.presentation.library.LibraryAction
import com.example.pagekeeper.pages.presentation.library.models.Screen
import com.example.pagekeeper.pages.presentation.util.getIconForScreen
import com.example.pagekeeper.pages.presentation.util.getStringForScreen

@Composable
fun LibraryNavigationDrawer(
    drawerState: DrawerState,
    selectedScreen: Screen,
    isEnabled: Boolean,
    onAction: (LibraryAction) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    if (isEnabled) {
        var drawerItemWidth by remember { mutableIntStateOf(Int.MAX_VALUE) }

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth(0.8f)
                    ) {
                        Spacer(Modifier.height(12.dp))
                        IconButton(
                            onClick = { onAction(LibraryAction.OnDrawerClose) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_menu_open_24),
                                contentDescription = stringResource(R.string.close_menu)
                            )
                        }
                        PrimaryButton(
                            text = stringResource(R.string.import_book),
                            onClick = { onAction(LibraryAction.OnImportBookClick) },
                            iconRes = R.drawable.import_book,
                            letterSpacing = (-0.15).sp,
                            modifier = Modifier
                                .onSizeChanged {
                                    drawerItemWidth = it.width
                                }
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Screen.entries.forEach { screen ->
                            val isActive = selectedScreen == screen
                            NavigationDrawerItem(
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
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MaterialTheme.colorScheme.onSurface,
                                    selectedIconColor = MaterialTheme.colorScheme.icons,
                                    unselectedIconColor = MaterialTheme.colorScheme.icons,
                                    selectedTextColor = MaterialTheme.colorScheme.icons,
                                    unselectedTextColor = MaterialTheme.colorScheme.icons,
                                ),
                                modifier = Modifier
                                    .width(with(LocalDensity.current) { drawerItemWidth.toDp() })
                            )
                        }
                    }
                }
            },
            modifier = modifier
        ) {
            content()
        }
    } else
        content()
}

@Preview(showBackground = true)
@Composable
private fun LibraryNavigationDrawerPreview() {
    PageKeeperTheme {
        LibraryNavigationDrawer(
            drawerState = rememberDrawerState(DrawerValue.Open),
            selectedScreen = Screen.BOOKMARKS,
            isEnabled = true,
            onAction = {}
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}