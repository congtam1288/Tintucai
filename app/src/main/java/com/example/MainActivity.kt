package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.aicontent.AiContentScreen
import com.example.designsystem.BackgroundLight
import com.example.designsystem.BanTinAiTheme
import com.example.home.HomeScreen
import com.example.library.LibraryScreen
import com.example.navigation.AppBottomNavBar
import com.example.navigation.Screen
import com.example.notifications.NotificationsScreen
import com.example.schedule.ScheduleScreen
import com.example.settings.SettingsScreen
import com.example.tiktok.TikTokScreen
import com.example.ui.MainViewModel
import com.example.video.VideoScreen

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.WHITE,
                android.graphics.Color.WHITE
            )
        )
        window.decorView.setBackgroundColor(android.graphics.Color.WHITE)
        setContent {
            BanTinAiTheme {
                MainAppContent(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    // BackHandler: return to Home when on any sub-screen
    if (currentScreen != Screen.Home) {
        BackHandler {
            currentScreen = Screen.Home
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundLight,
        bottomBar = {
            AppBottomNavBar(
                currentRoute = currentScreen.route,
                onNavigate = { screen ->
                    currentScreen = screen
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.Home -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToAiContent = { currentScreen = Screen.AiContent },
                        onNavigateToNotifications = { currentScreen = Screen.Notifications },
                        onNavigateToLibrary = { currentScreen = Screen.Library },
                        onNavigateToVideo = { currentScreen = Screen.Video }
                    )
                    Screen.Schedule -> ScheduleScreen(
                        viewModel = viewModel
                    )
                    Screen.AiContent -> AiContentScreen(
                        viewModel = viewModel,
                        onNavigateToVideo = { currentScreen = Screen.Video }
                    )
                    Screen.Library -> LibraryScreen(
                        viewModel = viewModel
                    )
                    Screen.Video -> VideoScreen(
                        viewModel = viewModel,
                        onNavigateToTikTok = { currentScreen = Screen.TikTok }
                    )
                    Screen.Notifications -> NotificationsScreen(
                        viewModel = viewModel
                    )
                    Screen.TikTok -> TikTokScreen(
                        viewModel = viewModel
                    )
                    Screen.Settings -> SettingsScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
