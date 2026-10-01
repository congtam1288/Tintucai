package com.example.aicontent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import com.example.data.SampleData
import com.example.designsystem.*
import com.example.model.PipelineStep
import com.example.model.StepState
import com.example.ui.MainViewModel

@Composable
fun AiContentScreen(
    viewModel: MainViewModel,
    onNavigateToVideo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps by viewModel.pipelineSteps.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val renderState by viewModel.videoEngine.renderState.collectAsState()
    val appConfig by viewModel.appConfig.collectAsState()

    var expandedStep by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tạo nội dung mới",
                    style = TypographyH1.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                IconButton(
                    onClick = { viewModel.startContentPipeline() },
                    enabled = !isGenerating && !renderState.isRendering,
                    modifier = Modifier.testTag("refresh_pipeline_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "Chạy lại",
                        tint = if (isGenerating) TextDisabled else PrimaryBlue
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Topic card
                item {
                    AppCard(
                        backgroundColor = PrimaryBlueLight,
                        borderColor = PrimaryBlue.copy(alpha = 0.2f),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "Chủ đề tự động hôm nay:",
                                style = TypographyCaption.copy(fontWeight = FontWeight.Bold),
                                color = PrimaryBlueDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Bản tin AI & Đột phá công nghệ toàn cầu (Tập 102)",
                                style = TypographyH3.copy(fontWeight = FontWeight.SemiBold),
                                color = TextPrimary
                            )
                        }
                    }
                }

                // 5 Pipeline Steps
                items(steps.size) { index ->
                    val step = steps[index]
                    val isExpanded = expandedStep == step.stepNumber

                    PipelineStepCard(
                        step = step,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            expandedStep = if (isExpanded) null else step.stepNumber
                        },
                        selectedVoiceId = appConfig.selectedVoiceId,
                        onVoiceSelect = { voiceId ->
                            viewModel.selectVoice(voiceId)
                        },
                        onVoicePreview = { voice ->
                            viewModel.previewVoice(voice)
                        }
                    )
                }

                // Video rendering progress if currently active
                if (renderState.isRendering) {
                    item {
                        AppCard(
                            backgroundColor = SurfaceWhite,
                            borderColor = PrimaryBlue
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Video Engine (FFmpeg)",
                                        style = TypographyH3.copy(fontWeight = FontWeight.Bold),
                                        color = PrimaryBlue
                                    )
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = PrimaryBlue
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = renderState.currentStep,
                                    style = TypographyBodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Bottom Button "Tạo video"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                PrimaryActionButton(
                    text = if (renderState.isRendering) "Đang dựng video..." else "Tạo video",
                    icon = Icons.Default.PlayArrow,
                    onClick = {
                        viewModel.renderCurrentVideo {
                            onNavigateToVideo()
                        }
                    },
                    enabled = !renderState.isRendering && !isGenerating,
                    testTag = "create_video_button"
                )
            }
        }
    }
}

@Composable
private fun PipelineStepCard(
    step: PipelineStep,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    selectedVoiceId: String,
    onVoiceSelect: (String) -> Unit,
    onVoicePreview: (com.example.model.TTSVoice) -> Unit
) {
    AppCard(
        onClick = onToggleExpand,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = step.title,
                        style = TypographyH3.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = step.subtitle,
                        style = TypographyBodySmall,
                        color = when (step.state) {
                            StepState.COMPLETED -> SuccessGreenText
                            StepState.IN_PROGRESS -> WarningAmberText
                            StepState.ERROR -> ErrorRedText
                            StepState.PENDING -> TextSecondary
                        }
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StepStatusIndicator(state = step.state)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Expandable details for each step
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    when (step.stepNumber) {
                        1 -> StepSourcesDetail()
                        2 -> StepScriptDetail()
                        3 -> StepScenesDetail()
                        4 -> StepImagesDetail()
                        5 -> StepTTSDetail(
                            selectedVoiceId = selectedVoiceId,
                            onVoiceSelect = onVoiceSelect,
                            onVoicePreview = onVoicePreview
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepStatusIndicator(state: StepState) {
    when (state) {
        StepState.COMPLETED -> {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Hoàn thành",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        StepState.IN_PROGRESS -> {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(WarningAmber),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
        StepState.PENDING -> {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, CardBorder, CircleShape)
            )
        }
        StepState.ERROR -> {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(ErrorRed),
                contentAlignment = Alignment.Center
            ) {
                Text("!", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StepSourcesDetail() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariantLight, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Text("Nguồn tin tức tự động đã crawl:", style = TypographyCaption.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(4.dp))
        Text("• Reuters Technology - AI & Compute", style = TypographyBodySmall)
        Text("• VnExpress Số hóa - Trí tuệ nhân tạo", style = TypographyBodySmall)
        Text("• TechCrunch - Breakthrough AI models", style = TypographyBodySmall)
    }
}

@Composable
private fun StepScriptDetail() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariantLight, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Text("Kịch bản tự động:", style = TypographyCaption.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "\"Bản Tin AI số 102: Khám phá những bước tiến vượt bậc của trí tuệ nhân tạo và công nghệ bán dẫn...\"",
            style = TypographyBodySmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun StepScenesDetail() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariantLight, RoundedCornerShape(8.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("Cấu trúc 6 Scene (00:59):", style = TypographyCaption.copy(fontWeight = FontWeight.Bold))
        Text("• Scene 1 (10s): Tiêu điểm mở đầu", style = TypographyBodySmall)
        Text("• Scene 2 (10s): Mô hình AI mới", style = TypographyBodySmall)
        Text("• Scene 3 (10s): Phần cứng & Chip bán dẫn", style = TypographyBodySmall)
        Text("• Scene 4 (10s): Ứng dụng thực tế", style = TypographyBodySmall)
        Text("• Scene 5 (10s): Công nghệ giọng nói", style = TypographyBodySmall)
        Text("• Scene 6 (09s): Kết thúc & Kêu gọi follow", style = TypographyBodySmall)
    }
}

@Composable
private fun StepImagesDetail() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariantLight, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Text("6 Ảnh độ phân giải 1080x1920 (9:16):", style = TypographyCaption.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(4.dp))
        Text("001.png • 002.png • 003.png • 004.png • 005.png • 006.png", style = TypographyBodySmall, color = PrimaryBlue)
    }
}

@Composable
private fun StepTTSDetail(
    selectedVoiceId: String,
    onVoiceSelect: (String) -> Unit,
    onVoicePreview: (com.example.model.TTSVoice) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariantLight, RoundedCornerShape(8.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("Chọn giọng đọc trong 8 giọng (4 Nam - 4 Nữ):", style = TypographyCaption.copy(fontWeight = FontWeight.Bold))

        SampleData.defaultVoices.forEach { voice ->
            val isSelected = voice.id == selectedVoiceId
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSelected) PrimaryBlueLight else Color.Transparent)
                    .clickable { onVoiceSelect(voice.id) }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.RecordVoiceOver,
                        contentDescription = null,
                        tint = if (isSelected) PrimaryBlue else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = voice.name,
                        style = TypographyBodySmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                        color = if (isSelected) PrimaryBlue else TextPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryBlue.copy(alpha = 0.1f),
                    modifier = Modifier.clickable { onVoicePreview(voice) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PlayCircle,
                            contentDescription = "Nghe thử",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Thử",
                            style = TypographyCaption.copy(color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}
