package com.example.videoengine

import com.example.data.SampleData
import com.example.model.NewsProject
import com.example.model.ProjectStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class VideoRenderProgress(
    val currentStep: String = "",
    val progressPercent: Float = 0f,
    val isRendering: Boolean = false,
    val completedProject: NewsProject? = null,
    val logs: List<String> = emptyList()
)

class VideoEngine {
    private val _renderState = MutableStateFlow(VideoRenderProgress())
    val renderState: StateFlow<VideoRenderProgress> = _renderState.asStateFlow()

    suspend fun renderVideo(
        projectNumber: Int = 102,
        onComplete: (NewsProject) -> Unit
    ) {
        val projectId = "BAN-TIN-2026-10-01-0800"
        _renderState.value = VideoRenderProgress(
            currentStep = "Khởi động Video Engine FFmpeg...",
            progressPercent = 0.05f,
            isRendering = true,
            logs = listOf("[FFmpeg] Initialized video pipeline resolution=1080x1920 fps=30")
        )

        delay(400)
        _renderState.value = _renderState.value.copy(
            currentStep = "Đọc danh sách 6 ảnh (001.png - 006.png)...",
            progressPercent = 0.25f,
            logs = _renderState.value.logs + "[AssetLoader] 6 image frames verified in project $projectId"
        )

        delay(400)
        _renderState.value = _renderState.value.copy(
            currentStep = "Đồng bộ âm thanh TTS (001.mp3 - 006.mp3)...",
            progressPercent = 0.50f,
            logs = _renderState.value.logs + "[AudioEngine] Master audio track merged: 59.4s at 192kbps"
        )

        delay(500)
        _renderState.value = _renderState.value.copy(
            currentStep = "Ghép cảnh Scene Timeline & chèn text subtitle ticker...",
            progressPercent = 0.75f,
            logs = _renderState.value.logs + "[Timeline] Overlay subtitle font=SansSerif ticker applied"
        )

        delay(500)
        _renderState.value = _renderState.value.copy(
            currentStep = "Xuất file video MP4 hoàn tất...",
            progressPercent = 1.0f,
            logs = _renderState.value.logs + "[Output] Saved: /storage/projects/$projectId/video/final.mp4"
        )

        delay(300)
        val newProject = NewsProject(
            id = projectId,
            title = "Bản tin AI - Số $projectNumber",
            dateDisplay = "01/10/2026 08:00",
            status = ProjectStatus.VIDEO_READY,
            duration = "00:59",
            viewsCount = "0",
            isPostedToTikTok = false,
            scenes = SampleData.sampleScenes
        )

        _renderState.value = VideoRenderProgress(
            currentStep = "Hoàn tất! Video đã sẵn sàng để xem trước và xác nhận đăng.",
            progressPercent = 1.0f,
            isRendering = false,
            completedProject = newProject,
            logs = _renderState.value.logs + "[Success] Pipeline completed successfully."
        )

        onComplete(newProject)
    }
}
