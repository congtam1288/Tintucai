package com.example.tiktok

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.*
import com.example.model.ProjectStatus
import com.example.ui.MainViewModel

@Composable
fun TikTokScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val tikTokState by viewModel.tikTokState.collectAsState()
    val projects by viewModel.projects.collectAsState()

    var showApiDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }

    val pendingVideos = projects.filter { !it.isPostedToTikTok && it.status == ProjectStatus.VIDEO_READY }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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
                    text = "TikTok kết nối",
                    style = TypographyH1.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.clickable { showApiDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Key,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Cấu hình API",
                            style = TypographyCaption.copy(fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card 1: Account Info
                item {
                    TikTokAccountCard(
                        isConnected = tikTokState.isConnected,
                        username = tikTokState.username,
                        onToggleConnection = { viewModel.toggleTikTok() }
                    )
                }

                // Section "Trạng thái đăng"
                item {
                    Text(
                        text = "Trạng thái đăng",
                        style = TypographyH2.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }

                // 4 Status Cards
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        TikTokStatusItem(
                            label = "Chờ xác nhận",
                            countText = "${tikTokState.pendingConfirmationCount} video",
                            icon = Icons.Outlined.Schedule,
                            iconBg = WarningAmberContainer,
                            iconColor = WarningAmber
                        )

                        TikTokStatusItem(
                            label = "Đã lên lịch",
                            countText = "${tikTokState.scheduledCount} video",
                            icon = Icons.Outlined.CalendarMonth,
                            iconBg = PrimaryBlueLight,
                            iconColor = PrimaryBlue
                        )

                        TikTokStatusItem(
                            label = "Đã đăng thành công",
                            countText = "${tikTokState.publishedCount} video",
                            icon = Icons.Outlined.CheckCircle,
                            iconBg = SuccessGreenContainer,
                            iconColor = SuccessGreen
                        )

                        TikTokStatusItem(
                            label = "Đã lỗi",
                            countText = "${tikTokState.failedCount} video",
                            icon = Icons.Outlined.ErrorOutline,
                            iconBg = ErrorRedContainer,
                            iconColor = ErrorRed
                        )
                    }
                }

                // If there are pending videos for explicit user confirmation
                if (pendingVideos.isNotEmpty()) {
                    item {
                        Text(
                            text = "Video sẵn sàng cần bạn xác nhận:",
                            style = TypographyH3.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryBlueDark
                        )
                    }

                    items(pendingVideos.size) { index ->
                        val video = pendingVideos[index]
                        AppCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(video.title, style = TypographyH3.copy(fontWeight = FontWeight.Bold))
                                    Text("Thời lượng: ${video.duration}", style = TypographyBodySmall, color = TextSecondary)
                                }
                                PrimaryActionButton(
                                    text = "ĐĂNG NGAY",
                                    onClick = { viewModel.confirmPostToTikTok(video.id) },
                                    modifier = Modifier.width(130.dp),
                                    height = 36.dp,
                                    testTag = "post_video_btn_${video.id}"
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Bottom CTA "Xem lịch sử đăng"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                PrimaryActionButton(
                    text = "Xem lịch sử đăng",
                    icon = Icons.Outlined.History,
                    onClick = { showHistoryDialog = true },
                    testTag = "view_tiktok_history_button"
                )
            }
        }
    }

    // API Config Dialog
    if (showApiDialog) {
        var clientKey by remember { mutableStateOf(tikTokState.clientKey) }
        var clientSecret by remember { mutableStateOf(tikTokState.clientSecret) }

        AlertDialog(
            onDismissRequest = { showApiDialog = false },
            title = {
                Text(text = "Cấu hình TikTok API", style = TypographyH2)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Sử dụng TikTok for Developers API (v2.0) với quyền video.upload",
                        style = TypographyBodySmall,
                        color = TextSecondary
                    )
                    OutlinedTextField(
                        value = clientKey,
                        onValueChange = { clientKey = it },
                        label = { Text("Client Key") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = clientSecret,
                        onValueChange = { clientSecret = it },
                        label = { Text("Client Secret") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.repository.updateTikTokApi(clientKey, clientSecret)
                        showApiDialog = false
                    }
                ) {
                    Text("Lưu", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showApiDialog = false }) {
                    Text("Hủy", color = TextSecondary)
                }
            }
        )
    }

    // History Dialog
    if (showHistoryDialog) {
        val publishedVideos = projects.filter { it.isPostedToTikTok }
        AlertDialog(
            onDismissRequest = { showHistoryDialog = false },
            title = {
                Text(text = "Lịch sử đăng TikTok", style = TypographyH2)
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(publishedVideos.size) { index ->
                        val v = publishedVideos[index]
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceVariantLight, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Text(v.title, style = TypographyH3.copy(fontWeight = FontWeight.Bold))
                            Text("Ngày đăng: ${v.dateDisplay} • Lượt xem: ${v.viewsCount}", style = TypographyCaption, color = TextSecondary)
                            Spacer(modifier = Modifier.height(2.dp))
                            StatusBadge("● ĐÃ ĐĂNG THÀNH CÔNG", StatusType.SUCCESS)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showHistoryDialog = false }) {
                    Text("Đóng", color = PrimaryBlue)
                }
            }
        )
    }
}

@Composable
private fun TikTokAccountCard(
    isConnected: Boolean,
    username: String,
    onToggleConnection: () -> Unit
) {
    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // TikTok Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = AppIcons.TikTok,
                        contentDescription = "TikTok",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Tài khoản TikTok",
                        style = TypographyH3.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = username,
                        style = TypographyBodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Connection Badge / Button
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onToggleConnection() }
                    .testTag("toggle_tiktok_connection_button"),
                shape = RoundedCornerShape(14.dp),
                color = if (isConnected) SuccessGreenContainer else ErrorRedContainer,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isConnected) SuccessGreen.copy(alpha = 0.4f) else ErrorRed.copy(alpha = 0.4f)
                )
            ) {
                Text(
                    text = if (isConnected) "● ĐÃ KẾT NỐI" else "● CHƯA KẾT NỐI",
                    style = TypographyCaption.copy(fontWeight = FontWeight.Bold),
                    color = if (isConnected) SuccessGreenText else ErrorRedText,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun TikTokStatusItem(
    label: String,
    countText: String,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color
) {
    AppCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = label,
                    style = TypographyH3.copy(fontWeight = FontWeight.Medium),
                    color = TextPrimary
                )
            }

            Text(
                text = countText,
                style = TypographyH3.copy(fontWeight = FontWeight.Bold),
                color = TextSecondary
            )
        }
    }
}
