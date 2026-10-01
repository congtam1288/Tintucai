package com.example.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.designsystem.AppIcons

sealed class Screen(
    val route: String,
    val title: String,
    val navLabel: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val isBrandIcon: Boolean = false
) {
    object Home : Screen(
        route = "home",
        title = "Trang chủ",
        navLabel = "Trang chủ",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Schedule : Screen(
        route = "schedule",
        title = "Lịch",
        navLabel = "Lịch",
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth
    )

    object AiContent : Screen(
        route = "ai_content",
        title = "AI & Nội dung",
        navLabel = "AI & ND",
        selectedIcon = Icons.Filled.SmartToy,
        unselectedIcon = Icons.Outlined.SmartToy
    )

    object Library : Screen(
        route = "library",
        title = "Thư viện",
        navLabel = "Thư viện",
        selectedIcon = Icons.Filled.PhotoLibrary,
        unselectedIcon = Icons.Outlined.PhotoLibrary
    )

    object Video : Screen(
        route = "video",
        title = "Video",
        navLabel = "Video",
        selectedIcon = Icons.Filled.Movie,
        unselectedIcon = Icons.Outlined.Movie
    )

    object Notifications : Screen(
        route = "notifications",
        title = "Thông báo",
        navLabel = "Thông báo",
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications
    )

    object TikTok : Screen(
        route = "tiktok",
        title = "TikTok",
        navLabel = "TikTok",
        selectedIcon = AppIcons.TikTok,
        unselectedIcon = AppIcons.TikTok,
        isBrandIcon = true
    )

    object Settings : Screen(
        route = "settings",
        title = "Cài đặt",
        navLabel = "Cài đặt",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    companion object {
        // 6 primary tabs requested for the bottom navigation bar:
        // Trang chủ | Lịch | AI & Nội dung | Thư viện | Thông báo | Cài đặt
        val primaryNavItems: List<Screen>
            get() = listOf(
                Home,
                Schedule,
                AiContent,
                Library,
                Notifications,
                Settings
            )

        val items: List<Screen>
            get() = listOf(
                Home,
                Schedule,
                AiContent,
                Library,
                Video,
                Notifications,
                TikTok,
                Settings
            )
    }
}
