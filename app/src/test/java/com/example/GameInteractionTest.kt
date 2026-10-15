package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.model.GamePhase
import com.example.timer.RoundTimer
import com.example.viewmodel.GameViewModel
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GameInteractionTest {
    private lateinit var vm: GameViewModel
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    @Before fun setup() { vm = GameViewModel(app, false); vm.toggleSoundEffects(); vm.dismissSplash(); vm.startNewGame() }
    @After fun cleanup() { vm.returnToSetup(); RoundTimer.stop(app) }
    @Test fun rapidCorrectTapsAwardOnePoint() {
        vm.startTurn(); repeat(10) { vm.onGuessCorrect() }
        assertEquals(1, vm.uiState.value.team1.score)
        assertEquals(1, vm.uiState.value.roundHistory.size)
    }
    @Test fun readyScreenCannotAwardPointsOrSkipTurns() {
        vm.onGuessCorrect(); vm.onGuessWrongOrPass(); vm.proceedToNextTurn()
        assertEquals(GamePhase.READY_TURN, vm.uiState.value.phase)
        assertEquals(0, vm.uiState.value.team1.score)
        assertTrue(vm.uiState.value.roundHistory.isEmpty())
    }
    @Test fun passDoesNotAwardPointsAndNextTurnIsIdempotent() {
        vm.startTurn(); vm.onGuessWrongOrPass(); repeat(5) { vm.proceedToNextTurn() }
        assertEquals(2, vm.uiState.value.activeTeamId)
        assertEquals(1, vm.uiState.value.currentRoundNumber)
        assertEquals(1, vm.uiState.value.team1.passedCount)
    }
    @Test fun pausedRoundCannotAwardPointsEvenBeforeUiCollectsPause() {
        vm.startTurn(); vm.togglePauseTimer(); vm.onGuessCorrect()
        assertEquals(0, vm.uiState.value.team1.score)
        assertTrue(vm.uiState.value.roundHistory.isEmpty())
    }
    @Test fun expiredDeadlineRejectsLateCorrectTap() {
        vm.returnToSetup(); vm.updateTimerDuration(30); vm.startNewGame(); vm.startTurn()
        org.robolectric.shadows.ShadowSystemClock.advanceBy(java.time.Duration.ofSeconds(31))
        vm.onGuessCorrect()
        assertEquals(0, vm.uiState.value.team1.score)
    }
    @Test fun correctAdvancesStraightToHandoffAndStartRevealsFilm() {
        vm.startTurn(); vm.onGuessCorrect()
        assertEquals(GamePhase.READY_TURN, vm.uiState.value.phase)
        assertEquals(2, vm.uiState.value.activeTeamId)
        assertNotNull(vm.uiState.value.lastRoundResult)
        vm.startTurn()
        assertTrue(vm.uiState.value.isCardRevealed)
        assertNull(vm.uiState.value.lastRoundResult)
    }
    @Test fun invalidDurationsAreRejected() {
        vm.returnToSetup(); vm.updateTimerDuration(600); vm.updateTimerDuration(9999)
        assertEquals(600, vm.uiState.value.settings.timerDurationSeconds)
    }
}
