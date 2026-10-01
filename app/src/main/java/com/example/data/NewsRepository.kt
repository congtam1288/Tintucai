package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object SampleData {
    val sampleScenes = listOf(
        SceneItem(
            sceneNumber = 1,
            text = "Chào mừng bạn đến với Bản Tin AI hôm nay. Tiêu điểm công nghệ toàn cầu cập nhật 24/7.",
            imagePrompt = "Futuristic city with holographic news ticker, cyberpunk sunrise, hyperrealistic 8k",
            imageFileName = "001.png",
            audioFileName = "001.mp3",
            durationSeconds = 10,
            visualPlaceholderColor = 0xFF1E3A8A
        ),
        SceneItem(
            sceneNumber = 2,
            text = "Mô hình trí tuệ nhân tạo thế hệ mới chính thức vượt mốc hàng triệu tokens ngữ cảnh.",
            imagePrompt = "Deep learning neural network glowing visualization with data particles",
            imageFileName = "002.png",
            audioFileName = "002.mp3",
            durationSeconds = 10,
            visualPlaceholderColor = 0xFF1E40AF
        ),
        SceneItem(
            sceneNumber = 3,
            text = "Các tập đoàn lớn công bố chuỗi chip AI tiên tiến, tối ưu hóa 40% năng lượng.",
            imagePrompt = "Quantum processor chip macro shot with electric light circuits",
            imageFileName = "003.png",
            audioFileName = "003.mp3",
            durationSeconds = 10,
            visualPlaceholderColor = 0xFF312E81
        ),
        SceneItem(
            sceneNumber = 4,
            text = "Ứng dụng AI tự động hóa video và truyền thông xã hội bùng nổ mạnh mẽ trên toàn cầu.",
            imagePrompt = "Abstract digital studio workstation with AI rendering holographic timeline",
            imageFileName = "004.png",
            audioFileName = "004.mp3",
            durationSeconds = 10,
            visualPlaceholderColor = 0xFF0F766E
        ),
        SceneItem(
            sceneNumber = 5,
            text = "Hệ thống giọng đọc AI đa phương ngữ đạt độ tự nhiên vượt trội so với phát thanh viên.",
            imagePrompt = "Sound waves pulsating around a glowing sound orb in high-tech studio",
            imageFileName = "005.png",
            audioFileName = "005.mp3",
            durationSeconds = 10,
            visualPlaceholderColor = 0xFF581C87
        ),
        SceneItem(
            sceneNumber = 6,
            text = "Cảm ơn quý vị đã theo dõi. Nhấn theo dõi để đón xem Bản Tin AI mỗi ngày lúc 08:00!",
            imagePrompt = "Clean modern TV news anchor desk with glowing AI logo background",
            imageFileName = "006.png",
            audioFileName = "006.mp3",
            durationSeconds = 9,
            visualPlaceholderColor = 0xFF1D4ED8
        )
    )

    val defaultVoices = listOf(
        TTSVoice("voice_1", "1. Nam Bắc 01", "Nam", "Bắc"),
        TTSVoice("voice_2", "2. Nam Bắc 02", "Nam", "Bắc"),
        TTSVoice("voice_3", "3. Nữ Bắc 01", "Nữ", "Bắc"),
        TTSVoice("voice_4", "4. Nữ Bắc 02", "Nữ", "Bắc"),
        TTSVoice("voice_5", "5. Nam Nam 01", "Nam", "Nam"),
        TTSVoice("voice_6", "6. Nam Nam 02", "Nam", "Nam"),
        TTSVoice("voice_7", "7. Nữ Nam 01", "Nữ", "Nam"),
        TTSVoice("voice_8", "8. Nữ Nam 02", "Nữ", "Nam")
    )
}

class NewsRepository {
    private val _projects = MutableStateFlow<List<NewsProject>>(
        listOf(
            NewsProject(
                id = "BAN-TIN-2026-10-01-0700",
                title = "Bản tin AI - Số 101",
                dateDisplay = "30/09/2026 08:00",
                status = ProjectStatus.COMPLETED,
                duration = "00:59",
                viewsCount = "1.2k",
                isPostedToTikTok = true,
                scenes = SampleData.sampleScenes
            ),
            NewsProject(
                id = "BAN-TIN-2026-09-30-0700",
                title = "Bản tin AI - Số 100",
                dateDisplay = "29/09/2026 08:00",
                status = ProjectStatus.WAITING_CONFIRMATION,
                duration = "00:58",
                viewsCount = "850",
                isPostedToTikTok = false,
                scenes = SampleData.sampleScenes
            ),
            NewsProject(
                id = "BAN-TIN-2026-09-29-0700",
                title = "Bản tin AI - Số 99",
                dateDisplay = "28/09/2026 08:00",
                status = ProjectStatus.PROCESSING,
                duration = "00:56",
                viewsCount = "0",
                isPostedToTikTok = false,
                scenes = SampleData.sampleScenes
            ),
            NewsProject(
                id = "BAN-TIN-2026-09-28-0700",
                title = "Bản tin AI - Số 98",
                dateDisplay = "27/09/2026 08:00",
                status = ProjectStatus.WAITING_VIDEO,
                duration = "00:57",
                viewsCount = "520",
                isPostedToTikTok = false,
                scenes = SampleData.sampleScenes
            )
        )
    )
    val projects: StateFlow<List<NewsProject>> = _projects.asStateFlow()

    private val _schedules = MutableStateFlow<List<ScheduleEntry>>(
        listOf(
            ScheduleEntry("sch_102", "Bản tin AI - Số 102", "01/10/2026 08:00", 1),
            ScheduleEntry("sch_103", "Bản tin AI - Số 103", "02/10/2026 08:00", 2),
            ScheduleEntry("sch_104", "Bản tin AI - Số 104", "03/10/2026 08:00", 3)
        )
    )
    val schedules: StateFlow<List<ScheduleEntry>> = _schedules.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "notif_1",
                title = "Video Bản tin AI - Số 101 đã sẵn sàng để xem trước.",
                timeAgo = "2 phút trước",
                category = NotificationCategory.TASK,
                iconType = NotificationIconType.VIDEO_READY
            ),
            NotificationItem(
                id = "notif_2",
                title = "Đã tạo xong 6/6 hình ảnh cho Bản tin AI - Số 102.",
                timeAgo = "10 phút trước",
                category = NotificationCategory.TASK,
                iconType = NotificationIconType.IMAGE_DONE
            ),
            NotificationItem(
                id = "notif_3",
                title = "TTS hoàn thành cho Bản tin AI - Số 102.",
                timeAgo = "15 phút trước",
                category = NotificationCategory.SYSTEM,
                iconType = NotificationIconType.TTS_DONE
            ),
            NotificationItem(
                id = "notif_4",
                title = "Đã đăng thành công Bản tin AI - Số 100 lên TikTok.",
                timeAgo = "1 giờ trước",
                category = NotificationCategory.SYSTEM,
                iconType = NotificationIconType.TIKTOK_POSTED
            ),
            NotificationItem(
                id = "notif_5",
                title = "Lịch đăng Bản tin AI - Số 103 vào 02/10/2026 08:00.",
                timeAgo = "2 giờ trước",
                category = NotificationCategory.SYSTEM,
                iconType = NotificationIconType.SCHEDULE_SET
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _tikTokState = MutableStateFlow(TikTokAccountState())
    val tikTokState: StateFlow<TikTokAccountState> = _tikTokState.asStateFlow()

    private val _appConfig = MutableStateFlow(AppConfig())
    val appConfig: StateFlow<AppConfig> = _appConfig.asStateFlow()

    fun postVideoToTikTok(projectId: String) {
        _projects.update { list ->
            list.map {
                if (it.id == projectId) it.copy(status = ProjectStatus.PUBLISHED, isPostedToTikTok = true) else it
            }
        }
        _tikTokState.update { state ->
            state.copy(
                pendingConfirmationCount = (state.pendingConfirmationCount - 1).coerceAtLeast(0),
                publishedCount = state.publishedCount + 1
            )
        }
        addNotification(
            title = "Đã xác nhận & đăng thành công video lên TikTok.",
            category = NotificationCategory.TASK,
            iconType = NotificationIconType.TIKTOK_POSTED
        )
    }

    fun addNotification(
        title: String,
        category: NotificationCategory = NotificationCategory.TASK,
        iconType: NotificationIconType = NotificationIconType.ALERT
    ) {
        val newNotif = NotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = title,
            timeAgo = "Vừa xong",
            category = category,
            iconType = iconType
        )
        _notifications.update { listOf(newNotif) + it }
    }

    fun addSchedule(title: String, time: String, day: Int) {
        val entry = ScheduleEntry(
            id = "sch_${System.currentTimeMillis()}",
            title = title,
            scheduledTime = time,
            dayOfMonth = day
        )
        _schedules.update { it + entry }
        addNotification(
            title = "Đã thêm lịch phát sóng mới: $title vào $time.",
            category = NotificationCategory.SYSTEM,
            iconType = NotificationIconType.SCHEDULE_SET
        )
    }

    fun updateConfig(config: AppConfig) {
        _appConfig.value = config
    }

    fun toggleTikTokConnection() {
        _tikTokState.update { it.copy(isConnected = !it.isConnected) }
    }

    fun updateTikTokApi(clientKey: String, clientSecret: String) {
        _tikTokState.update { it.copy(clientKey = clientKey, clientSecret = clientSecret) }
    }
}
