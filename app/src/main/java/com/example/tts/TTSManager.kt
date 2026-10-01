package com.example.tts

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import kotlin.concurrent.thread
import kotlin.math.sin

class TTSManager(private val context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        try {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale("vi", "VN"))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts?.setLanguage(Locale.US)
                    }
                    isInitialized = true
                }
            }
        } catch (e: Exception) {
            Log.e("TTSManager", "Initialization error", e)
        }
    }

    fun speak(text: String, voiceId: String) {
        // Adjust pitch and speech rate depending on voice type
        val pitch = when (voiceId) {
            "voice_1" -> 0.95f // Nam Bắc 01
            "voice_2" -> 0.90f // Nam Bắc 02
            "voice_3" -> 1.15f // Nữ Bắc 01
            "voice_4" -> 1.25f // Nữ Bắc 02
            "voice_5" -> 0.92f // Nam Nam 01
            "voice_6" -> 0.88f // Nam Nam 02
            "voice_7" -> 1.10f // Nữ Nam 01
            "voice_8" -> 1.20f // Nữ Nam 02
            else -> 1.0f
        }

        val speechRate = when (voiceId) {
            "voice_2", "voice_6" -> 1.05f
            "voice_3", "voice_7" -> 1.02f
            else -> 0.98f
        }

        tts?.setPitch(pitch)
        tts?.setSpeechRate(speechRate)

        val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "sample_utterance")
        if (result != TextToSpeech.SUCCESS) {
            // Fallback: generate a short pleasant melodic audio preview tone
            playBeepFeedback(pitch)
        }
    }

    private fun playBeepFeedback(pitchMultiplier: Float) {
        thread {
            try {
                val sampleRate = 44100
                val durationMs = 250
                val numSamples = (durationMs * sampleRate) / 1000
                val sample = ShortArray(numSamples)
                val freq = 440.0 * pitchMultiplier

                for (i in 0 until numSamples) {
                    val angle = 2.0 * Math.PI * i / (sampleRate / freq)
                    sample[i] = (sin(angle) * 32767 * 0.4).toInt().toShort()
                }

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(numSamples * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(sample, 0, numSamples)
                track.play()
            } catch (e: Exception) {
                Log.w("TTSManager", "AudioTrack fallback error", e)
            }
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}
