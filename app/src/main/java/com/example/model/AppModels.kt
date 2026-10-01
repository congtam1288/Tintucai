package com.example.model

enum class ProjectStatus {
    PROCESSING,             // Đang xử lý
    COMPLETED,              // Đã hoàn thành
    WAITING_VIDEO,          // Chờ video
    WAITING_CONFIRMATION,   // Chờ xác nhận đăng
    PUBLISHED,              // Đã đăng
    VIDEO_READY,            // Video sẵn sàng
    GENERATING,             // Đang tạo
    SCHEDULED               // Đã lên lịch
}

data class SceneItem(
    val sceneNumber: Int,
    val text: String,
    val imagePrompt: String,
    val imageFileName: String,
    val audioFileName: String,
    val durationSeconds: Int = 10,
    val visualPlaceholderColor: Long = 0xFF1E3A8A
)

data class NewsProject(
    val id: String,                 // e.g. "BAN-TIN-2026-10-01-0700"
    val title: String,              // e.g. "Bản tin AI - Số 101"
    val dateDisplay: String,        // e.g. "30/09/2026 08:00"
    val status: ProjectStatus,
    val duration: String = "00:59",
    val resolution: String = "1080x1920",
    val viewsCount: String = "1.2k",
    val likesCount: String = "142",
    val scenes: List<SceneItem> = emptyList(),
    val isPostedToTikTok: Boolean = false,
    val scheduledTime: String? = null
)

data class ScheduleEntry(
    val id: String,
    val title: String,
    val scheduledTime: String,
    val dayOfMonth: Int,
    val month: Int = 10,
    val year: Int = 2026,
    val isActive: Boolean = true
)

enum class StepState {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    ERROR
}

data class PipelineStep(
    val stepNumber: Int,
    val title: String,
    val subtitle: String,
    val state: StepState
)

data class NotificationItem(
    val id: String,
    val title: String,
    val timeAgo: String,
    val category: NotificationCategory,
    val isRead: Boolean = false,
    val iconType: NotificationIconType
)

enum class NotificationCategory {
    ALL,
    SYSTEM,
    TASK
}

enum class NotificationIconType {
    VIDEO_READY,
    IMAGE_DONE,
    TTS_DONE,
    TIKTOK_POSTED,
    SCHEDULE_SET,
    ALERT
}

data class TTSVoice(
    val id: String,
    val name: String,
    val gender: String, // "Nam" or "Nữ"
    val region: String, // "Bắc" or "Nam"
    val sampleText: String = "Chào mừng quý khán giả đến với Bản Tin AI hôm nay."
)

data class TikTokAccountState(
    val isConnected: Boolean = true,
    val username: String = "@bantinaiofficial",
    val displayName: String = "Bản Tin AI Official",
    val pendingConfirmationCount: Int = 2,
    val scheduledCount: Int = 1,
    val publishedCount: Int = 5,
    val failedCount: Int = 0,
    val clientKey: String = "aw7v9xk02plm9",
    val clientSecret: String = "••••••••••••••••"
)

data class AppConfig(
    val geminiApiKey: String = "",
    val geminiModel: String = "gemini-2.5-flash",
    val selectedVoiceId: String = "voice_1",
    val videoResolution: String = "1080x1920 (9:16)",
    val videoFps: Int = 30,
    val videoQuality: String = "Cao (High Bitrate)",
    val notificationsEnabled: Boolean = true,
    val language: String = "Tiếng Việt",
    val themeMode: String = "Sáng"
)
