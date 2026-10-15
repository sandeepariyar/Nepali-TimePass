package com.example.timer

/** Pure monotonic clock. Rounding only affects display, never the stored deadline. */
class DeadlineClock(private val now: () -> Long) {
    var deadline: Long = 0L
        private set
    var pausedMillis: Long = 0L
        private set
    var running: Boolean = false
        private set
    fun start(millis: Long) { pausedMillis = millis.coerceAtLeast(0); resume() }
    fun remainingMillis(): Long = if (running) (deadline - now()).coerceAtLeast(0) else pausedMillis
    fun remainingSeconds(): Int = ((remainingMillis() + 999) / 1000).toInt()
    fun pause() { pausedMillis = remainingMillis(); running = false }
    fun resume() { deadline = now() + pausedMillis; running = pausedMillis > 0 }
    fun stop() { running = false; pausedMillis = 0; deadline = 0 }
}
