package com.example.timer

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.audio.GameSoundEffects
import kotlinx.coroutines.*

/** A bounded user-started game countdown; no exact alarm, overlays, or battery exemption. */
class RoundTimerService : Service() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var job: Job? = null
    private var soundJob: Job? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private lateinit var audio: GameSoundEffects
    private val manager get() = getSystemService(NotificationManager::class.java)
    override fun onCreate() {
        super.onCreate()
        audio = GameSoundEffects(this)
        soundJob = scope.launch { RoundTimer.sound.collect { audio.isEnabled = it } }
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(NotificationChannel(CHANNEL, "Active game round", NotificationManager.IMPORTANCE_LOW).apply {
                description = "Countdown and pause controls for your active round"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setSound(null, null)
            })
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == PAUSE) { RoundTimer.toggle(this); stopSelf(); return START_NOT_STICKY }
        val state = RoundTimer.tick()
        if (!state.running && !state.expired) { stopSelf(); return START_NOT_STICKY }
        try {
            if (Build.VERSION.SDK_INT >= 34) startForeground(ID, notification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
            else startForeground(ID, notification())
        } catch (_: SecurityException) { RoundTimer.serviceUnavailable(); stopSelf(); return START_NOT_STICKY }
        job?.cancel()
        wakeLock?.let { if (it.isHeld) it.release() }
        wakeLock = (getSystemService(POWER_SERVICE) as PowerManager).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "$packageName:round").apply {
            setReferenceCounted(false)
            acquire(((RoundTimer.deadline() - SystemClock.elapsedRealtime()).coerceAtLeast(0) + 4000).coerceAtMost(604000))
        }
        job = scope.launch {
            var lastSecond = -1
            while (isActive) {
                val current = RoundTimer.tick()
                if (current.session != state.session || (!current.running && !current.expired)) break
                if (current.expired) {
                    audio.isEnabled = RoundTimer.soundEnabled
                    audio.playTimeUpAlarm()?.join()
                    break
                }
                if (current.remainingSeconds != lastSecond) {
                    lastSecond = current.remainingSeconds
                    // Chronometer runs in SystemUI; update text without revealing the secret movie.
                    if (Build.VERSION.SDK_INT < 33 || checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        manager.notify(ID, notification())
                    }
                    if (lastSecond in 1..10) {
                        audio.isEnabled = RoundTimer.soundEnabled
                        if (RoundTimer.consumeTenSecondWarning(lastSecond)) audio.playTenSecondWarning()
                        else audio.playTick(1f + (10 - lastSecond) * .05f)
                    }
                }
                delay(100)
            }
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
        return START_NOT_STICKY
    }
    private fun notification(): Notification {
        val open = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val pause = PendingIntent.getService(this, 1, Intent(this, RoundTimerService::class.java).setAction(PAUSE), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, CHANNEL)
            .setSmallIcon(R.drawable.ic_round_timer)
            .setContentTitle(if (RoundTimer.nepali) "नेपाली टाइमपास • अभिनयको पालो" else "Nepali TimePass • Acting round")
            .setContentText(if (RoundTimer.nepali) "${RoundTimer.state.value.remainingSeconds} सेकेन्ड बाँकी" else "${RoundTimer.state.value.remainingSeconds}s remaining")
            .setWhen(System.currentTimeMillis() + (RoundTimer.deadline() - SystemClock.elapsedRealtime()).coerceAtLeast(0))
            .setUsesChronometer(true).setChronometerCountDown(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH).setOngoing(true)
            .setContentIntent(open).addAction(0, if (RoundTimer.nepali) "रोक्नुहोस्" else "Pause", pause)
            .build()
    }
    override fun onDestroy() {
        job?.cancel(); soundJob?.cancel(); scope.cancel(); audio.close()
        stopForeground(STOP_FOREGROUND_REMOVE)
        wakeLock?.let { if (it.isHeld) it.release() }; wakeLock = null
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? = null
    companion object { const val CHANNEL = "active_round"; const val ID = 7001; const val PAUSE = "com.example.PAUSE_ROUND" }
}
