package com.example.audio

import android.content.Context
import android.media.*
import android.os.Build
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.*

/** One bounded voice, low amplitude, cancellable playback and deterministic native release. */
class GameSoundEffects(context: Context) : AutoCloseable {
    private val manager = context.applicationContext.getSystemService(AudioManager::class.java)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mutex = Mutex()
    @Volatile var isEnabled = true
    @Volatile private var lostFocus = false
    private val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
    private val listener = AudioManager.OnAudioFocusChangeListener { change -> lostFocus = change < 0 }
    private val request = if (Build.VERSION.SDK_INT >= 26) AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
        .setAudioAttributes(attributes).setOnAudioFocusChangeListener(listener).build() else null
    fun playTick(pitchMultiplier: Float = 1f) = cue(doubleArrayOf(180.0 * pitchMultiplier), 70, true)
    fun playUiTap() = cue(doubleArrayOf(440.0), 45, true)
    fun playCorrectDing() = cue(doubleArrayOf(587.33, 880.0), 150)
    fun playWrongBuzz() = cue(doubleArrayOf(220.0, 164.81), 120, true)
    fun playTimeUpAlarm() = cue(doubleArrayOf(196.0, 392.0, 196.0, 523.25), 220, true)
    fun playWinnerFanfare() = cue(doubleArrayOf(523.25, 587.33, 783.99, 880.0, 1046.5), 150, true)
    private fun cue(notes: DoubleArray, noteMs: Int, drum: Boolean = false): Job? {
        if (!isEnabled) return null
        return scope.launch {
            mutex.withLock {
                if (!isEnabled) return@withLock
                // Honor silent/vibrate mode and device audio policy; never change volume or DND.
                if (manager.ringerMode != AudioManager.RINGER_MODE_NORMAL) return@withLock
                lostFocus = false
                @Suppress("DEPRECATION")
                val focused = if (Build.VERSION.SDK_INT >= 26) manager.requestAudioFocus(request!!) else
                    manager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                if (focused != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) return@withLock
                var track: AudioTrack? = null
                try {
                    val rate = 22050
                    val length = rate * noteMs * notes.size / 1000
                    val pcm = ShortArray(length) { i ->
                        val t = i.toDouble() / rate
                        val index = (i * 1000 / rate / noteMs).coerceAtMost(notes.lastIndex)
                        val local = t % (noteMs / 1000.0)
                        val envelope = (local / .006).coerceAtMost(1.0) * exp(-local * if (drum) 22 else 12)
                        val tone = sin(2 * PI * notes[index] * local)
                        val overtone = if (drum) .35 * sin(2 * PI * notes[index] * 1.52 * local) else 0.0
                        ((tone + overtone) * envelope * .18 * Short.MAX_VALUE).toInt().toShort()
                    }
                    track = AudioTrack.Builder().setAudioAttributes(attributes)
                        .setAudioFormat(AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(rate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                        .setBufferSizeInBytes(pcm.size * 2).setTransferMode(AudioTrack.MODE_STATIC).build()
                    if (track.state != AudioTrack.STATE_INITIALIZED) return@withLock
                    track.write(pcm, 0, pcm.size); track.play()
                    var elapsed = 0
                    while (elapsed < noteMs * notes.size && !lostFocus && isEnabled) { delay(20); elapsed += 20 }
                } catch (_: IllegalArgumentException) {
                    // Unsupported output device: gameplay continues silently.
                } catch (_: IllegalStateException) {
                    // Output disconnected during cue.
                } finally {
                    try { track?.stop() } catch (_: IllegalStateException) { }
                    track?.release()
                    if (Build.VERSION.SDK_INT >= 26) manager.abandonAudioFocusRequest(request!!)
                    else { @Suppress("DEPRECATION") manager.abandonAudioFocus(listener) }
                }
            }
        }
    }
    override fun close() { isEnabled = false; scope.cancel() }
}
