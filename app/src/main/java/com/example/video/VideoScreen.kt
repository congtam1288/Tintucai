package com.example.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
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
fun VideoScreen(
    viewModel: MainViewModel,
    onNavigateToTikTok: () -> Unit,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.projects.collectAsState()
    val previewVideo by viewModel.previewVideo.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Tất cả", "Sẵn sàng (2)", "Đã đăng")

    var selectedVideoForModal by remember { mutableStateOf<NewsProject?>(null) }
    var isPlayingPreview by remember { mutableStateOf(false) }

    val filteredProjects = when (selectedTabIndex) {
        1 -> projects.filter { it.status == ProjectStatus.VIDEO_READY }
        2 -> projects.filter { it.status == ProjectStatus.PUBLISHED }
        else -> projects
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
                text = "Video đã tạo",
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

        // Video list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredProjects.size) { index ->
                val project = filteredProjects[index]
                VideoItemCard(
                    project = project,
                    onClick = { selectedVideoForModal = project }
                )
            }
        }
    }

    // Video Detail & TikTok Confirmation Modal
    if (selectedVideoForModal != null) {
        val video = selectedVideoForModal!!
        AlertDialog(
            onDismissRequest = {
                selectedVideoForModal = null
                isPlayingPreview = false
            },
            title = {
                Text(text = video.title, style = TypographyH2)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Simulated 9:16 Vertical Player Frame
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E3A8A))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isPlayingPreview) SuccessGreen else PrimaryBlue,
                                modifier = Modifier
                                    .clickable { isPlayingPreview = !isPlayingPreview }
                                    .testTag("play_video_preview_button")
                            ) {
                                Box(
                                    modifier = Modifier.size(48.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Xem video",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isPlayingPreview) "▶ Đang phát video 1080x1920..." else "Nhấn để phát video",
                                style = TypographyCaption.copy(color = Color.White)
                            )
                        }

                        // Bottom News Ticker inside player
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.7f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "BẢN TIN AI • Tiêu điểm công nghệ 24/7",
                                style = TypographyCaption.copy(color = Color.White, fontSize = 10.sp),
                                maxLines = 1
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Thời lượng: ${video.duration}", style = TypographyBodySmall)
                        Text("Kích thước: 1080x1920", style = TypographyBodySmall)
                    }

                    if (video.isPostedToTikTok) {
                        StatusBadge("Đã đăng lên TikTok", StatusType.SUCCESS)
                    } else {
                        StatusBadge("Chờ xác nhận đăng TikTok", StatusType.WARNING)
                    }
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!video.isPostedToTikTok) {
                        Button(
                            onClick = {
                                viewModel.confirmPostToTikTok(video.id)
                                selectedVideoForModal = null
                                onNavigateToTikTok()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("confirm_post_tiktok_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = AppIcons.TikTok,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("XÁC NHẬN ĐĂNG TIKTOK", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            selectedVideoForModal = null
                            isPlayingPreview = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Đóng", color = TextPrimary)
                    }
                }
            },
            dismissButton = null
        )
    }
}

@Composable
private fun VideoItemCard(
    project: NewsProject,
    onClick: () -> Unit
) {
    AppCard(
        onClick = onClick,
        contentPadding = PaddingValues(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 9:16 Video Thumbnail Preview
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = project.title,
                    style = TypographyH3.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = project.dateDisplay,
                    style = TypographyBodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${project.duration} • ${project.resolution}",
                    style = TypographyCaption,
                    color = TextTertiary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = project.viewsCount,
                        style = TypographyCaption,
                        color = TextTertiary
                    )
                }
            }

            // Status Badge on the right
            val (badgeText, badgeType) = when (project.status) {
                ProjectStatus.COMPLETED, ProjectStatus.PUBLISHED -> "Đã hoàn thành" to StatusType.SUCCESS
                ProjectStatus.WAITING_CONFIRMATION, ProjectStatus.VIDEO_READY -> "Chờ xác nhận" to StatusType.PURPLE
                ProjectStatus.PROCESSING, ProjectStatus.GENERATING -> "Đang xử lý" to StatusType.WARNING
                ProjectStatus.WAITING_VIDEO, ProjectStatus.SCHEDULED -> "Chờ video" to StatusType.INFO
            }

            StatusBadge(text = badgeText, statusType = badgeType)
        }
    }
}
