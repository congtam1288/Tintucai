package com.example.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.*
import com.example.model.NewsProject
import com.example.model.ProjectStatus
import com.example.ui.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToAiContent: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToVideo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.projects.collectAsState()
    var isScheduleAutoEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Header: “BẢN TIN AI” + Trạng thái: “Hệ thống đang hoạt động”
        HomeScreenHeader(
            statusText = "Hệ thống đang hoạt động",
            onNotificationClick = onNavigateToNotifications
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // CARD 1 — BẢN TIN HÔM NAY: Tên bản tin, ngày giờ, trạng thái và tiến trình
            item {
                Card1TodayNews(
                    newsTitle = "Bản tin AI - Số 102",
                    dateTime = "30/09/2026 • 07:00",
                    statusText = "Đang xử lý",
                    progressPercent = 0.75f,
                    progressLabel = "Tiến độ: 6/8 bước (75%)"
                )
            }

            // CARD 2 — QUY TRÌNH: 8 bước cụ thể
            item {
                Card2PipelineProcess(
                    onDetailsClick = onNavigateToAiContent
                )
            }

            // CARD 3 — LỊCH TỰ ĐỘNG: Hiển thị “07:00 – 08:00”, cho phép bật/tắt lịch
            item {
                Card3AutoSchedule(
                    timeRange = "07:00 – 08:00",
                    isEnabled = isScheduleAutoEnabled,
                    onToggleEnabled = { isScheduleAutoEnabled = it }
                )
            }

            // CARD 5 — TRẠNG THÁI: Ảnh, Audio, Video, TikTok
            item {
                Card5ResourceStatus()
            }

            // CARD 4 — DỰ ÁN GẦN ĐÂY: Mỗi dự án có tên riêng, ngày giờ riêng và trạng thái riêng
            item {
                Card4RecentProjects(
                    projects = projects.take(4),
                    onProjectClick = { project ->
                        viewModel.selectProject(project)
                        if (project.status == ProjectStatus.COMPLETED ||
                            project.status == ProjectStatus.WAITING_CONFIRMATION ||
                            project.status == ProjectStatus.PUBLISHED ||
                            project.status == ProjectStatus.VIDEO_READY
                        ) {
                            viewModel.openVideoPreview(project)
                            onNavigateToVideo()
                        } else {
                            onNavigateToLibrary()
                        }
                    },
                    onSeeAllClick = onNavigateToLibrary
                )
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        // NÚT CHÍNH: “+ Tạo bản tin mới”
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            PrimaryActionButton(
                text = "+ Tạo bản tin mới",
                icon = Icons.Default.Add,
                onClick = onNavigateToAiContent,
                testTag = "create_new_news_button"
            )
        }
    }
}

/**
 * Header: “BẢN TIN AI” + Trạng thái: “Hệ thống đang hoạt động”
 */
@Composable
private fun HomeScreenHeader(
    statusText: String,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AiBrandBadge(size = 28.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BẢN TIN AI",
                    style = TypographyH1.copy(fontWeight = FontWeight.ExtraBold),
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            // Trạng thái: “Hệ thống đang hoạt động”
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = statusText,
                    style = TypographyCaption.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = SuccessGreenText
                    )
                )
            }
        }

        Box {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("notification_bell_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Thông báo",
                    tint = TextPrimary
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(ErrorRed)
            )
        }
    }
}

/**
 * CARD 1 — BẢN TIN HÔM NAY: Tên bản tin, ngày giờ, trạng thái và tiến trình
 */
@Composable
private fun Card1TodayNews(
    newsTitle: String,
    dateTime: String,
    statusText: String,
    progressPercent: Float,
    progressLabel: String
) {
    AppCard(
        backgroundColor = SurfaceWhite,
        borderColor = CardBorder
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "BẢN TIN HÔM NAY",
                        style = TypographyCaption.copy(fontWeight = FontWeight.Bold, color = PrimaryBlue),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = newsTitle,
                        style = TypographyH2.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }

                StatusBadge(
                    text = statusText,
                    statusType = StatusType.WARNING
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = dateTime,
                style = TypographyBodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tiến trình
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = progressLabel,
                    style = TypographyCaption.copy(fontWeight = FontWeight.Medium, color = TextPrimary)
                )
                Text(
                    text = "${(progressPercent * 100).toInt()}%",
                    style = TypographyH3.copy(fontWeight = FontWeight.Bold, color = PrimaryBlue)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progressPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = PrimaryBlue,
                trackColor = CardBorder
            )
        }
    }
}

/**
 * CARD 2 — QUY TRÌNH:
 * ① Thu thập dữ liệu
 * ② Viết bài
 * ③ Chia cảnh
 * ④ Tạo ảnh
 * ⑤ Tạo audio
 * ⑥ Dựng video
 * ⑦ Chờ xác nhận đăng
 * ⑧ Đăng TikTok
 */
@Composable
private fun Card2PipelineProcess(
    onDetailsClick: () -> Unit
) {
    AppCard(
        backgroundColor = SurfaceWhite,
        borderColor = CardBorder
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "QUY TRÌNH TỰ ĐỘNG",
                    style = TypographyCaption.copy(fontWeight = FontWeight.Bold, color = PrimaryBlue),
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = "Chi tiết ›",
                    style = TypographyCaption.copy(fontWeight = FontWeight.Bold, color = PrimaryBlue),
                    modifier = Modifier.clickable { onDetailsClick() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val processSteps = listOf(
                Triple("①", "Thu thập dữ liệu", ProcessStatus.DONE),
                Triple("②", "Viết bài", ProcessStatus.DONE),
                Triple("③", "Chia cảnh", ProcessStatus.DONE),
                Triple("④", "Tạo ảnh", ProcessStatus.DONE),
                Triple("⑤", "Tạo audio", ProcessStatus.DONE),
                Triple("⑥", "Dựng video", ProcessStatus.RUNNING),
                Triple("⑦", "Chờ xác nhận đăng", ProcessStatus.WAITING),
                Triple("⑧", "Đăng TikTok", ProcessStatus.WAITING)
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                processSteps.forEach { (indexSymbol, stepName, status) ->
                    ProcessStepRow(
                        indexSymbol = indexSymbol,
                        name = stepName,
                        status = status
                    )
                }
            }
        }
    }
}

private enum class ProcessStatus {
    DONE, RUNNING, WAITING
}

@Composable
private fun ProcessStepRow(
    indexSymbol: String,
    name: String,
    status: ProcessStatus
) {
    val (statusLabel, statusType) = when (status) {
        ProcessStatus.DONE -> "Hoàn thành" to StatusType.SUCCESS
        ProcessStatus.RUNNING -> "Đang xử lý" to StatusType.WARNING
        ProcessStatus.WAITING -> "Chờ" to StatusType.INFO
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(if (status == ProcessStatus.RUNNING) PrimaryBlueLight.copy(alpha = 0.35f) else Color.Transparent)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = indexSymbol,
                style = TypographyCaption.copy(fontWeight = FontWeight.Bold, color = if (status == ProcessStatus.DONE) PrimaryBlue else TextSecondary),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                style = TypographyBodySmall.copy(
                    fontWeight = if (status == ProcessStatus.RUNNING) FontWeight.Bold else FontWeight.Medium,
                    color = TextPrimary
                )
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (status == ProcessStatus.DONE) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            } else if (status == ProcessStatus.RUNNING) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 2.dp,
                    color = WarningAmber
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            StatusBadge(
                text = statusLabel,
                statusType = statusType
            )
        }
    }
}

/**
 * CARD 3 — LỊCH TỰ ĐỘNG: Hiển thị “07:00 – 08:00”, cho phép bật/tắt lịch
 */
@Composable
private fun Card3AutoSchedule(
    timeRange: String,
    isEnabled: Boolean,
    onToggleEnabled: (Boolean) -> Unit
) {
    AppCard(
        backgroundColor = SurfaceWhite,
        borderColor = CardBorder
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimaryBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "LỊCH TỰ ĐỘNG",
                        style = TypographyCaption.copy(fontWeight = FontWeight.Bold, color = PrimaryBlue),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = timeRange,
                        style = TypographyH3.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = if (isEnabled) "Tự động chạy hàng ngày lúc 07:00" else "Đã tạm dừng lịch",
                        style = TypographyCaption,
                        color = TextSecondary
                    )
                }
            }

            Switch(
                checked = isEnabled,
                onCheckedChange = onToggleEnabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = PrimaryBlue
                ),
                modifier = Modifier.testTag("toggle_auto_schedule_switch")
            )
        }
    }
}

/**
 * CARD 5 — TRẠNG THÁI:
 * Ảnh: hoàn thành
 * Audio: hoàn thành
 * Video: chờ xử lý/đã hoàn thành
 * TikTok: chờ xác nhận
 */
@Composable
private fun Card5ResourceStatus() {
    AppCard(
        backgroundColor = SurfaceWhite,
        borderColor = CardBorder
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "TRẠNG THÁI TÀI NGUYÊN",
                style = TypographyCaption.copy(fontWeight = FontWeight.Bold, color = PrimaryBlue),
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2x2 Grid of Resource Statuses
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResourceStatusTile(
                    label = "Ảnh",
                    statusText = "Hoàn thành",
                    detail = "6/6 ảnh (001-006)",
                    statusType = StatusType.SUCCESS,
                    modifier = Modifier.weight(1f)
                )

                ResourceStatusTile(
                    label = "Audio",
                    statusText = "Hoàn thành",
                    detail = "6/6 file TTS",
                    statusType = StatusType.SUCCESS,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResourceStatusTile(
                    label = "Video",
                    statusText = "Chờ xử lý",
                    detail = "FFmpeg MP4",
                    statusType = StatusType.INFO,
                    modifier = Modifier.weight(1f)
                )

                ResourceStatusTile(
                    label = "TikTok",
                    statusText = "Chờ xác nhận",
                    detail = "Cần duyệt thủ công",
                    statusType = StatusType.PURPLE,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ResourceStatusTile(
    label: String,
    statusText: String,
    detail: String,
    statusType: StatusType,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = BackgroundLight,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = TypographyH3.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                    color = TextPrimary
                )
                StatusBadge(text = statusText, statusType = statusType)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = detail,
                style = TypographyCaption.copy(fontSize = 11.sp),
                color = TextSecondary
            )
        }
    }
}

/**
 * CARD 4 — DỰ ÁN GẦN ĐÂY:
 * Mỗi dự án có tên riêng, ngày giờ riêng và trạng thái riêng.
 * Không được trộn dữ liệu giữa các dự án.
 */
@Composable
private fun Card4RecentProjects(
    projects: List<NewsProject>,
    onProjectClick: (NewsProject) -> Unit,
    onSeeAllClick: () -> Unit
) {
    AppCard(
        backgroundColor = SurfaceWhite,
        borderColor = CardBorder
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DỰ ÁN GẦN ĐÂY",
                    style = TypographyCaption.copy(fontWeight = FontWeight.Bold, color = PrimaryBlue),
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = "Xem tất cả ›",
                    style = TypographyCaption.copy(fontWeight = FontWeight.Bold, color = PrimaryBlue),
                    modifier = Modifier
                        .clickable { onSeeAllClick() }
                        .padding(2.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                projects.forEachIndexed { index, project ->
                    RecentProjectItemRow(
                        project = project,
                        onClick = { onProjectClick(project) }
                    )

                    if (index < projects.lastIndex) {
                        Divider(color = DividerColor, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentProjectItemRow(
    project: NewsProject,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryBlueLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Movie,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = project.title,
                    style = TypographyH3.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                    color = TextPrimary
                )
                Text(
                    text = "${project.dateDisplay} • ${project.duration}",
                    style = TypographyCaption.copy(fontSize = 11.sp),
                    color = TextSecondary
                )
            }
        }

        val (badgeText, badgeType) = when (project.status) {
            ProjectStatus.COMPLETED, ProjectStatus.PUBLISHED -> "Đã hoàn thành" to StatusType.SUCCESS
            ProjectStatus.WAITING_CONFIRMATION -> "Chờ xác nhận đăng" to StatusType.PURPLE
            ProjectStatus.PROCESSING, ProjectStatus.GENERATING -> "Đang xử lý" to StatusType.WARNING
            ProjectStatus.WAITING_VIDEO, ProjectStatus.SCHEDULED -> "Chờ video" to StatusType.INFO
            ProjectStatus.VIDEO_READY -> "Chờ xác nhận đăng" to StatusType.PURPLE
        }

        StatusBadge(
            text = badgeText,
            statusType = badgeType
        )
    }
}
