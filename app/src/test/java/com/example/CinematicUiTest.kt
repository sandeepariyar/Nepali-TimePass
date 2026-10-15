package com.example

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.model.*
import com.example.ui.screens.ActingScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameUiState
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.*

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h640dp-xxhdpi")
class CinematicUiTest {
    @get:Rule val compose = createComposeRule()
    private fun render(nepali: Boolean, revealed: Boolean = false) {
        compose.setContent {
            var reveal by remember { mutableStateOf(revealed) }
            MyApplicationTheme {
                ActingScreen(GameUiState(phase = GamePhase.ACTING,
                    appLanguage = if (nepali) AppLanguage.NEPALI else AppLanguage.ENGLISH,
                    currentMovie = Movie("qa", "The Courtyard After Dark", "काठमाडौँको साँझको चलचित्र", 2026, MovieLanguage.NEPALI, "Drama", posterUrl = "file:///nonexistent-test-poster"),
                    isCardRevealed = reveal, isTimerPaused = true, remainingSeconds = 9),
                    onToggleReveal = { reveal = !reveal }, onTogglePauseTimer = {}, onGuessCorrect = {},
                    onGuessWrongOrPass = {}, onSkipMovie = {}, onConcludeGame = {})
            }
        }
    }
    @Test fun secretMovieStaysHiddenUntilReveal() {
        render(false)
        compose.onNodeWithTag("movie_title").assertDoesNotExist()
        compose.onNodeWithTag("reveal_movie_button").performClick()
        compose.onNodeWithTag("movie_title").assertTextEquals("The Courtyard After Dark")
    }
    @Test fun pausedControlsAreDisabledButResumeRemainsAccessible() {
        render(true, true)
        compose.onNodeWithTag("guess_correct_button").assertIsNotEnabled()
        compose.onNodeWithTag("guess_wrong_button").assertIsNotEnabled()
        compose.onNodeWithTag("pause_timer_button").assertIsEnabled().assertIsDisplayed()
        compose.onNodeWithTag("countdown").assertTextEquals("00:09")
    }
    @Test fun devanagariSmallPhoneScreenshot() {
        render(true, true)
        compose.onNodeWithTag("movie_title").assertTextEquals("काठमाडौँको साँझको चलचित्र")
        compose.onRoot().captureRoboImage("build/reports/cinematic-nepali-small.png")
    }
}
