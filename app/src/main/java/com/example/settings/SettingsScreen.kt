package com.example.settings

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.BrightnessMedium
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.MovieFilter
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.SampleData
import com.example.designsystem.*
import com.example.model.AppConfig
import com.example.ui.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val appConfig by viewModel.appConfig.collectAsState()

    var showGoogleAiDialog by remember { mutableStateOf(false) }
    var showTtsDialog by remember { mutableStateOf(false) }
    var showVideoEngineDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cài đặt",
                style = TypographyH1.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        // Settings items list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                SettingsCardItem(
                    icon = Icons.Outlined.SmartToy,
                    title = "Cấu hình Google AI",
                    subtitle = "Gemini Flash, Gemini Pro API",
                    onClick = { showGoogleAiDialog = true }
                )
            }

            item {
                val selectedVoice = SampleData.defaultVoices.find { it.id == appConfig.selectedVoiceId }
                SettingsCardItem(
                    icon = Icons.Outlined.RecordVoiceOver,
                    title = "Giọng đọc (TTS)",
                    subtitle = selectedVoice?.name ?: "8 giọng (4 Nam - 4 Nữ)",
                    onClick = { showTtsDialog = true }
                )
            }

            item {
                SettingsCardItem(
                    icon = Icons.Outlined.MovieFilter,
                    title = "Cài đặt dựng video",
                    subtitle = "FFmpeg, định dạng, chất lượng",
                    onClick = { showVideoEngineDialog = true }
                )
            }

            item {
                SettingsCardToggle(
                    icon = Icons.Outlined.Notifications,
                    title = "Thông báo Android",
                    subtitle = if (appConfig.notificationsEnabled) "Bật" else "Tắt",
                    checked = appConfig.notificationsEnabled,
                    onCheckedChange = {
                        viewModel.updateConfig(appConfig.copy(notificationsEnabled = it))
                    }
                )
            }

            item {
                SettingsCardItem(
                    icon = Icons.Outlined.Backup,
                    title = "Sao lưu & khôi phục",
                    subtitle = "Lưu trữ cấu hình dự án JSON",
                    onClick = {
                        viewModel.repository.addNotification(
                            title = "Đã xuất sao lưu cấu hình ứng dụng thành công.",
                            category = com.example.model.NotificationCategory.SYSTEM
                        )
                    }
                )
            }

            item {
                SettingsCardItem(
                    icon = Icons.Outlined.Language,
                    title = "Ngôn ngữ",
                    subtitle = appConfig.language,
                    onClick = { /* Language selector */ }
                )
            }

            item {
                SettingsCardItem(
                    icon = Icons.Outlined.BrightnessMedium,
                    title = "Giao diện",
                    subtitle = appConfig.themeMode,
                    onClick = { /* Theme selector */ }
                )
            }

            item {
                SettingsCardItem(
                    icon = Icons.Outlined.Info,
                    title = "Thông tin ứng dụng",
                    subtitle = "Phiên bản 1.0.0",
                    onClick = { showAboutDialog = true }
                )
            }
        }
    }

    // Google AI Config Dialog
    if (showGoogleAiDialog) {
        var apiKey by remember { mutableStateOf(appConfig.geminiApiKey) }
        var selectedModel by remember { mutableStateOf(appConfig.geminiModel) }

        AlertDialog(
            onDismissRequest = { showGoogleAiDialog = false },
            title = {
                Text(text = "Cấu hình Google AI", style = TypographyH2)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Sử dụng API Gemini để tự động tạo kịch bản, tóm tắt tin tức và prompt hình ảnh.",
                        style = TypographyBodySmall,
                        color = TextSecondary
                    )
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text("Gemini API Key") },
                        placeholder = { Text("Nhập API key từ Google AI Studio") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Model AI:", style = TypographyCaption.copy(fontWeight = FontWeight.Bold))
                    listOf("gemini-2.5-flash", "gemini-2.5-pro").forEach { model ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedModel = model }
                        ) {
                            RadioButton(
                                selected = selectedModel == model,
                                onClick = { selectedModel = model }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(model, style = TypographyBodySmall)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateConfig(appConfig.copy(geminiApiKey = apiKey, geminiModel = selectedModel))
                        showGoogleAiDialog = false
                    }
                ) {
                    Text("Lưu", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoogleAiDialog = false }) {
                    Text("Hủy", color = TextSecondary)
                }
            }
        )
    }

    // 8 Voice Selector Dialog
    if (showTtsDialog) {
        AlertDialog(
            onDismissRequest = { showTtsDialog = false },
            title = {
                Text(text = "Chọn giọng đọc AI (8 giọng)", style = TypographyH2)
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(SampleData.defaultVoices.size) { index ->
                        val voice = SampleData.defaultVoices[index]
                        val isSelected = voice.id == appConfig.selectedVoiceId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PrimaryBlueLight else Color.Transparent)
                                .clickable {
                                    viewModel.selectVoice(voice.id)
                                }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = voice.name,
                                style = TypographyBodySmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                                color = if (isSelected) PrimaryBlue else TextPrimary
                            )
                            TextButton(onClick = { viewModel.previewVoice(voice) }) {
                                Text("Nghe thử", color = PrimaryBlue, style = TypographyCaption)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTtsDialog = false }) {
                    Text("Xong", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Video Engine Dialog
    if (showVideoEngineDialog) {
        AlertDialog(
            onDismissRequest = { showVideoEngineDialog = false },
            title = {
                Text(text = "Cài đặt Video Engine (FFmpeg)", style = TypographyH2)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Độ phân giải xuất: 1080x1920 (9:16 TikTok chuẩn)", style = TypographyBodySmall)
                    Text("• Tốc độ khung hình: 30 FPS", style = TypographyBodySmall)
                    Text("• Video Codec: H.264 / AAC", style = TypographyBodySmall)
                    Text("• Hiệu ứng chuyển cảnh: Smooth Pan & Zoom", style = TypographyBodySmall)
                    Text("• Tự động gắn phụ đề tiếng Việt (Auto Subtitle Ticker)", style = TypographyBodySmall)
                }
            },
            confirmButton = {
                TextButton(onClick = { showVideoEngineDialog = false }) {
                    Text("Đóng", color = PrimaryBlue)
                }
            }
        )
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AiBrandBadge(size = 28.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Bản Tin AI", style = TypographyH2)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Phiên bản: 1.0.0 (Release 2026)", style = TypographyBodySmall)
                    Text("Ứng dụng tự động hóa toàn bộ quy trình từ tin tức đến video ngắn TikTok.", style = TypographyBodySmall, color = TextSecondary)
                    Text("Tích hợp Google AI, Imagen, TTS 8 giọng, FFmpeg Video Engine.", style = TypographyBodySmall, color = TextSecondary)
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Đóng", color = PrimaryBlue)
                }
            }
        )
    }
}

@Composable
private fun SettingsCardItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    AppCard(
        onClick = onClick,
        contentPadding = PaddingValues(12.dp)
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
                        imageVector = icon,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        style = TypographyH3.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        style = TypographyBodySmall,
                        color = TextSecondary
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun SettingsCardToggle(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    AppCard(contentPadding = PaddingValues(12.dp)) {
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
                        imageVector = icon,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        style = TypographyH3.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        style = TypographyBodySmall,
                        color = TextSecondary
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = PrimaryBlue
                )
            )
        }
    }
}
