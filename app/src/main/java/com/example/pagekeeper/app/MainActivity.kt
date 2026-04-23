package com.example.pagekeeper.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.pagekeeper.app.navigation.NavigationRoot
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value == 0
            }
        }

        enableEdgeToEdge()
        setContent {
            PageKeeperTheme {
                NavigationRoot()
            }
        }
    }
}
