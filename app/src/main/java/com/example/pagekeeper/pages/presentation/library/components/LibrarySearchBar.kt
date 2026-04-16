@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.icons
import com.example.pagekeeper.pages.presentation.library.models.ScreenType

@Composable
fun LibrarySearchBar(
    textFieldState: TextFieldState,
    screenType: ScreenType,
    isTabletSearchBarEnabled: Boolean,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        state = textFieldState,
        enabled = isTabletSearchBarEnabled,
        placeholder = {
            Text(
               text = stringResource(R.string.search_books),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        trailingIcon = {
            when(screenType) {
                ScreenType.SEARCH -> {
                    IconButton(
                        onClick = { textFieldState.clearText() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_close_24),
                            contentDescription = stringResource(R.string.remove_search_query),
                            tint = MaterialTheme.colorScheme.icons
                        )
                    }
                }
                else -> {
                    IconButton(
                        onClick = onSearchClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_search_24),
                            contentDescription = stringResource(R.string.remove_search_query),
                            tint = MaterialTheme.colorScheme.icons
                        )
                    }
                }
            }

        },
        lineLimits = TextFieldLineLimits.SingleLine,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondary,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSecondary,
            cursorColor = MaterialTheme.colorScheme.onSecondary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
            .then(
                if (screenType == ScreenType.SEARCH)
                    Modifier.fillMaxWidth()
                else
                    Modifier.fillMaxWidth(0.5f)
            )
    )
}

@Preview
@Composable
private fun LibrarySearchBarPreview() {
    LibrarySearchBar(
        textFieldState = rememberTextFieldState(),
        screenType = ScreenType.LIST,
        isTabletSearchBarEnabled = true,
        onSearchClick = {}
    )
}