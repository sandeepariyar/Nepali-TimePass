package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class GameSoundEffects {

    private val scope = CoroutineScope(Dispatchers.Default)
    var isEnabled: Boolean = true

    fun playTick(pitchMultiplier: Float = 1.0f) {
        if (!isEnabled) return
        scope.launch {
            val sampleRate = 44100
            val durationMs = 40
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)
            val freq = 1200.0 * pitchMultiplier

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val envelope = 1.0 - (i.toDouble() / numSamples) // decay
                val sample = (sin(2.0 * Math.PI * freq * t) * envelope * 0.4 * Short.MAX_VALUE).toInt()
                buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    fun playCorrectDing() {
        if (!isEnabled) return
        scope.launch {
            val sampleRate = 44100
            val durationMs = 350
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val freq = if (t < 0.12) 880.0 else 1320.0 // Major jump
                val envelope = (1.0 - (i.toDouble() / numSamples)).coerceAtLeast(0.0)
                val sample = (sin(2.0 * Math.PI * freq * t) * envelope * 0.6 * Short.MAX_VALUE).toInt()
                buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    fun playWrongBuzz() {
        if (!isEnabled) return
        scope.launch {
            val sampleRate = 44100
            val durationMs = 400
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val freq = 180.0 - (t * 60.0) // Descending low buzz
                val square = if (sin(2.0 * Math.PI * freq * t) > 0) 1.0 else -1.0
                val envelope = 1.0 - (i.toDouble() / numSamples)
                val sample = (square * envelope * 0.4 * Short.MAX_VALUE).toInt()
                buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    fun playTimeUpAlarm() {
        if (!isEnabled) return
        scope.launch {
            val sampleRate = 44100
            val durationMs = 1200
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                // Alternating alarm beep: 600Hz & 850Hz modulation every 0.15s
                val cycle = ((t / 0.15).toInt()) % 2
                val freq = if (cycle == 0) 650.0 else 900.0
                val square = if (sin(2.0 * Math.PI * freq * t) > 0) 1.0 else -1.0
                val sample = (square * 0.5 * Short.MAX_VALUE).toInt()
                buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    fun playWinnerFanfare() {
        if (!isEnabled) return
        scope.launch {
            val sampleRate = 44100
            val durationMs = 1000
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            // C major arpeggio notes: C5 (523Hz), E5 (659Hz), G5 (784Hz), C6 (1046Hz)
            val notes = doubleArrayOf(523.25, 659.25, 783.99, 1046.50)
            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val noteIdx = (t / 0.22).toInt().coerceIn(0, notes.size - 1)
                val freq = notes[noteIdx]
                val noteT = t % 0.22
                val envelope = 1.0 - (noteT / 0.22) * 0.4
                val sample = (sin(2.0 * Math.PI * freq * t) * envelope * 0.6 * Short.MAX_VALUE).toInt()
                buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    private fun playPcmBuffer(buffer: ShortArray, sampleRate: Int) {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val trackSize = buffer.size.coerceAtLeast(minBufferSize)
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(trackSize * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            Thread.sleep((buffer.size * 1000L / sampleRate) + 50)
            audioTrack.stop()
            audioTrack.release()
        } catch (_: Exception) {
            // Ignore audio track creation errors on non-supported environments
        }
    }
}
