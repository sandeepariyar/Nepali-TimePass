package com.example.timer

import org.junit.Assert.*
import org.junit.Test

class DeadlineClockTest {
    @Test fun delayedCallbacksCatchUpWithoutDrift() {
        var now = 0L; val clock = DeadlineClock { now }
        clock.start(180_000); now = 42_600
        assertEquals(138, clock.remainingSeconds())
        now = 180_001; assertEquals(0, clock.remainingSeconds())
    }
    @Test fun pausesRetainSubsecondPrecision() {
        var now = 0L; val clock = DeadlineClock { now }
        clock.start(30_000); now = 1_650; clock.pause()
        now = 90_000; assertEquals(28_350L, clock.remainingMillis())
        clock.resume(); now += 28_349
        assertEquals(1, clock.remainingSeconds()); now++
        assertEquals(0, clock.remainingSeconds())
    }
    @Test fun repeatedPauseResumeDoesNotAddSeconds() {
        var now = 0L; val clock = DeadlineClock { now }; clock.start(60_000)
        repeat(100) { now += 5; clock.pause(); now += 10; clock.resume() }
        assertEquals(59_500, clock.remainingMillis().toInt())
    }
    @Test fun stopClearsDeadlineAndPendingTime() {
        val clock = DeadlineClock { 100L }; clock.start(30_000); clock.stop()
        assertFalse(clock.running); assertEquals(0, clock.remainingSeconds()); assertEquals(0L, clock.deadline)
    }
    @Test fun wallClockChangesCannotAffectElapsedClock() {
        var elapsed = 500L; val clock = DeadlineClock { elapsed }; clock.start(60_000)
        elapsed = 60_499; assertEquals(1, clock.remainingSeconds())
        elapsed = 70_000; assertEquals(0, clock.remainingSeconds())
    }
}
