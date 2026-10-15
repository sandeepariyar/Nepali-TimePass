package com.example.timer

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RoundTimerState(val remainingSeconds: Int = 0, val running: Boolean = false,
    val expired: Boolean = false, val session: Long = 0, val unavailable: Boolean = false)

/** Commands and service callbacks all run on the main thread. No wall-clock arithmetic. */
object RoundTimer {
    private val clock = DeadlineClock(SystemClock::elapsedRealtime)
    private val mutable = MutableStateFlow(RoundTimerState())
    val state = mutable.asStateFlow()
    private val soundMutable = MutableStateFlow(true)
    val sound = soundMutable.asStateFlow()
    var soundEnabled: Boolean
        get() = soundMutable.value
        set(value) { soundMutable.value = value }
    var nepali = true
    private var sessionCounter = 0L
    fun start(context: Context, seconds: Int, sound: Boolean, isNepali: Boolean) {
        soundEnabled = sound; nepali = isNepali
        clock.start(seconds * 1000L)
        mutable.value = RoundTimerState(seconds, true, session = ++sessionCounter)
        startService(context)
    }
    private fun startService(context: Context) {
        try { ContextCompat.startForegroundService(context, Intent(context, RoundTimerService::class.java)) }
        catch (_: IllegalStateException) { mutable.value = mutable.value.copy(unavailable = true) }
        catch (_: SecurityException) { mutable.value = mutable.value.copy(unavailable = true) }
    }
    fun tick(): RoundTimerState {
        val seconds = clock.remainingSeconds()
        mutable.value = mutable.value.copy(remainingSeconds = seconds,
            expired = mutable.value.expired || (clock.running && seconds == 0), running = clock.running && seconds > 0)
        return mutable.value
    }
    fun toggle(context: Context) {
        if (tick().expired || mutable.value.session == 0L) return
        if (clock.running) {
            clock.pause(); tick()
            mutable.value = mutable.value.copy(running = false)
            context.stopService(Intent(context, RoundTimerService::class.java))
        } else { clock.resume(); tick(); startService(context) }
    }
    fun stop(context: Context) {
        clock.stop(); mutable.value = RoundTimerState()
        context.stopService(Intent(context, RoundTimerService::class.java))
    }
    fun restore(context: Context, deadline: Long, remaining: Long, running: Boolean, sound: Boolean, isNepali: Boolean) {
        soundEnabled = sound; nepali = isNepali
        clock.start(if (running) (deadline - SystemClock.elapsedRealtime()).coerceAtLeast(0) else remaining)
        if (!running) clock.pause()
        mutable.value = RoundTimerState(clock.remainingSeconds(), clock.running,
            expired = running && clock.remainingMillis() == 0L, session = ++sessionCounter)
        if (clock.running) startService(context)
    }
    fun remainingMillis() = clock.remainingMillis()
    fun deadline() = clock.deadline
    fun serviceUnavailable() { mutable.value = mutable.value.copy(unavailable = true) }
}
