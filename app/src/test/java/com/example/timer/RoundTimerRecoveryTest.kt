package com.example.timer

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.GameSessionStore
import com.example.model.*
import com.example.viewmodel.GameUiState
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowSystemClock
import java.time.Duration

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RoundTimerRecoveryTest {
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    @After fun stop() { RoundTimer.stop(app) }
    @Test fun noUiCallbacksAreNeededToRecoverExpiredCountdown() {
        RoundTimer.start(app, 30, false, true)
        ShadowSystemClock.advanceBy(Duration.ofSeconds(31))
        assertTrue(RoundTimer.tick().expired)
        assertEquals(0, RoundTimer.tick().remainingSeconds)
    }
    @Test fun restoredPauseKeepsSubsecondRemainderAcrossLongGap() {
        RoundTimer.restore(app, 0L, 12_345L, false, false, false)
        ShadowSystemClock.advanceBy(Duration.ofMinutes(4))
        assertEquals(12_345L, RoundTimer.remainingMillis())
        assertFalse(RoundTimer.tick().running)
        RoundTimer.toggle(app)
        ShadowSystemClock.advanceBy(Duration.ofMillis(12_344))
        assertEquals(1, RoundTimer.tick().remainingSeconds)
        ShadowSystemClock.advanceBy(Duration.ofMillis(1))
        assertTrue(RoundTimer.tick().expired)
    }
    @Test fun savedSettingsCustomFilmsAndScoresRoundTrip() {
        val store = GameSessionStore(app)
        val movie = Movie("custom_qa", "A custom film", "आफ्नै चलचित्र", 2026, MovieLanguage.NEPALI, "Drama")
        val game = GameUiState(phase = GamePhase.ACTING, appLanguage = AppLanguage.ENGLISH,
            team1 = Team(1, "Custom gang", 0xFF16A34A, "", score = 4), currentMovie = movie,
            settings = GameSettings(timerDurationSeconds = 90, soundEffectsEnabled = false,
                selectedLanguages = setOf(MovieLanguage.NEPALI)))
        store.save(game, 123_000, 30_123, true); store.saveCustom(listOf(movie))
        val restored = GameSessionStore(app)
        assertEquals(game, restored.load()!!.game)
        assertEquals(123_000L, restored.load()!!.deadline)
        assertEquals(30_123L, restored.load()!!.remaining)
        assertEquals(listOf(movie), restored.loadCustom())
    }
}
