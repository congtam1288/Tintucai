package com.example.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.designsystem.*
import com.example.model.NotificationCategory
import com.example.model.NotificationIconType
import com.example.model.NotificationItem
import com.example.ui.MainViewModel

@Composable
fun NotificationsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Tất cả", "Hệ thống", "Công việc")

    val filteredNotifications = when (selectedTabIndex) {
        1 -> notifications.filter { it.category == NotificationCategory.SYSTEM }
        2 -> notifications.filter { it.category == NotificationCategory.TASK }
        else -> notifications
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Screen Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Thông báo",
                style = TypographyH1.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        // Filter Tabs
        FilterChipBar(
            tabs = tabs,
            selectedIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Notifications List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredNotifications.size) { index ->
                val item = filteredNotifications[index]
                NotificationCard(item = item)
            }
        }
    }
}

@Composable
private fun NotificationCard(item: NotificationItem) {
    AppCard(contentPadding = PaddingValues(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            val (icon, bgColor, tintColor) = when (item.iconType) {
                NotificationIconType.VIDEO_READY -> Triple(Icons.Outlined.Videocam, PrimaryBlueLight, PrimaryBlue)
                NotificationIconType.IMAGE_DONE -> Triple(Icons.Outlined.Image, InfoCyanContainer, InfoCyan)
                NotificationIconType.TTS_DONE -> Triple(Icons.Outlined.RecordVoiceOver, PurpleAccentContainer, PurpleAccent)
                NotificationIconType.TIKTOK_POSTED -> Triple(Icons.Outlined.Check, WarningAmberContainer, WarningAmber)
                NotificationIconType.SCHEDULE_SET -> Triple(Icons.Outlined.CalendarMonth, PrimaryBlueLight, PrimaryBlue)
                NotificationIconType.ALERT -> Triple(Icons.Outlined.Notifications, ErrorRedContainer, ErrorRed)
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = TypographyH3.copy(fontWeight = FontWeight.Medium),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.timeAgo,
                    style = TypographyCaption,
                    color = TextSecondary
                )
            }
        }
    }
}
