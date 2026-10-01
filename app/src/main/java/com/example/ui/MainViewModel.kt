package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NewsRepository
import com.example.data.SampleData
import com.example.model.*
import com.example.tts.TTSManager
import com.example.videoengine.VideoEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val repository = NewsRepository()
    private val ttsManager = TTSManager(application.applicationContext)
    val videoEngine = VideoEngine()

    val projects = repository.projects
    val schedules = repository.schedules
    val notifications = repository.notifications
    val tikTokState = repository.tikTokState
    val appConfig = repository.appConfig

    // Selected project for Library / Video Inspection
    private val _selectedProject = MutableStateFlow<NewsProject?>(null)
    val selectedProject: StateFlow<NewsProject?> = _selectedProject.asStateFlow()

    // Video Player Preview Dialog State
    private val _previewVideo = MutableStateFlow<NewsProject?>(null)
    val previewVideo: StateFlow<NewsProject?> = _previewVideo.asStateFlow()

    // AI Generation Pipeline Steps
    private val _pipelineSteps = MutableStateFlow(
        listOf(
            PipelineStep(1, "1. Thu thập tin tức", "• Đang thu thập...", StepState.IN_PROGRESS),
            PipelineStep(2, "2. Viết bài", "• Đã tạo", StepState.COMPLETED),
            PipelineStep(3, "3. Chia cảnh (Scene)", "• Đã chia 6 cảnh", StepState.COMPLETED),
            PipelineStep(4, "4. Tạo hình ảnh", "• Đã tạo 6/6 ảnh", StepState.COMPLETED),
            PipelineStep(5, "5. Giọng đọc (TTS)", "• Chọn giọng đọc", StepState.PENDING)
        )
    )
    val pipelineSteps: StateFlow<List<PipelineStep>> = _pipelineSteps.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    init {
        _selectedProject.value = projects.value.firstOrNull()
    }

    fun selectProject(project: NewsProject) {
        _selectedProject.value = project
    }

    fun openVideoPreview(project: NewsProject) {
        _previewVideo.value = project
    }

    fun closeVideoPreview() {
        _previewVideo.value = null
    }

    fun previewVoice(voice: TTSVoice) {
        ttsManager.speak(voice.sampleText, voice.id)
    }

    fun selectVoice(voiceId: String) {
        repository.updateConfig(appConfig.value.copy(selectedVoiceId = voiceId))
        _pipelineSteps.update { steps ->
            steps.map { step ->
                if (step.stepNumber == 5) {
                    val voiceName = SampleData.defaultVoices.find { it.id == voiceId }?.name ?: voiceId
                    step.copy(subtitle = "• Đã chọn $voiceName", state = StepState.COMPLETED)
                } else step
            }
        }
    }

    fun startContentPipeline(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _isGenerating.value = true

            _pipelineSteps.value = listOf(
                PipelineStep(1, "1. Thu thập tin tức", "• Đang quét 5 kênh tin công nghệ...", StepState.IN_PROGRESS),
                PipelineStep(2, "2. Viết bài", "• Chưa bắt đầu", StepState.PENDING),
                PipelineStep(3, "3. Chia cảnh (Scene)", "• Chưa bắt đầu", StepState.PENDING),
                PipelineStep(4, "4. Tạo hình ảnh", "• Chưa bắt đầu", StepState.PENDING),
                PipelineStep(5, "5. Giọng đọc (TTS)", "• Chưa bắt đầu", StepState.PENDING)
            )

            kotlinx.coroutines.delay(800)
            _pipelineSteps.update { steps ->
                steps.map {
                    when (it.stepNumber) {
                        1 -> it.copy(subtitle = "• Đã thu thập 5 nguồn tin", state = StepState.COMPLETED)
                        2 -> it.copy(subtitle = "• Đang viết kịch bản AI...", state = StepState.IN_PROGRESS)
                        else -> it
                    }
                }
            }

            kotlinx.coroutines.delay(800)
            _pipelineSteps.update { steps ->
                steps.map {
                    when (it.stepNumber) {
                        2 -> it.copy(subtitle = "• Đã tạo kịch bản 6 đoạn", state = StepState.COMPLETED)
                        3 -> it.copy(subtitle = "• Đang phân bổ 6 cảnh...", state = StepState.IN_PROGRESS)
                        else -> it
                    }
                }
            }

            kotlinx.coroutines.delay(700)
            _pipelineSteps.update { steps ->
                steps.map {
                    when (it.stepNumber) {
                        3 -> it.copy(subtitle = "• Đã chia 6 cảnh", state = StepState.COMPLETED)
                        4 -> it.copy(subtitle = "• Đang render 6 hình ảnh...", state = StepState.IN_PROGRESS)
                        else -> it
                    }
                }
            }

            kotlinx.coroutines.delay(800)
            _pipelineSteps.update { steps ->
                steps.map {
                    when (it.stepNumber) {
                        4 -> it.copy(subtitle = "• Đã tạo 6/6 ảnh", state = StepState.COMPLETED)
                        5 -> it.copy(subtitle = "• Đang tổng hợp TTS...", state = StepState.IN_PROGRESS)
                        else -> it
                    }
                }
            }

            kotlinx.coroutines.delay(800)
            _pipelineSteps.update { steps ->
                steps.map {
                    when (it.stepNumber) {
                        5 -> it.copy(subtitle = "• Đã chọn Nam Bắc 01 (Hoàn tất)", state = StepState.COMPLETED)
                        else -> it
                    }
                }
            }

            _isGenerating.value = false
            repository.addNotification(
                title = "Đã hoàn thành tạo nội dung AI cho Bản tin mới.",
                category = NotificationCategory.TASK,
                iconType = NotificationIconType.IMAGE_DONE
            )
            onComplete()
        }
    }

    fun renderCurrentVideo(onRenderFinished: () -> Unit = {}) {
        viewModelScope.launch {
            videoEngine.renderVideo(projectNumber = 102) { newProject ->
                _selectedProject.value = newProject
                repository.addNotification(
                    title = "Video ${newProject.title} đã sẵn sàng để xem trước.",
                    category = NotificationCategory.TASK,
                    iconType = NotificationIconType.VIDEO_READY
                )
                onRenderFinished()
            }
        }
    }

    fun confirmPostToTikTok(projectId: String) {
        repository.postVideoToTikTok(projectId)
        _previewVideo.value = null
    }

    fun addSchedule(title: String, time: String, day: Int) {
        repository.addSchedule(title, time, day)
    }

    fun toggleTikTok() {
        repository.toggleTikTokConnection()
    }

    fun updateConfig(config: AppConfig) {
        repository.updateConfig(config)
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
