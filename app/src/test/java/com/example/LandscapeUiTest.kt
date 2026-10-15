package com.example

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
@Config(sdk = [34], qualifiers = "w800dp-h360dp-land-xxhdpi")
class LandscapeUiTest {
    @get:Rule val compose = createComposeRule()
    @Test fun landscapeFilmAndControlsRemainAccessible() {
        compose.setContent { MyApplicationTheme {
            ActingScreen(GameUiState(phase = GamePhase.ACTING, isTimerPaused = true, isCardRevealed = true,
                currentMovie = Movie("qa", "A Very Long Cinema Title", "लामो नाम भएको नेपाली चलचित्र", 2026, MovieLanguage.NEPALI, "Drama")), {}, {}, {}, {}, {}, {})
        } }
        compose.onNodeWithTag("movie_title").assertIsDisplayed()
        compose.onNodeWithTag("pause_timer_button").performScrollTo().assertIsDisplayed()
        compose.onRoot().captureRoboImage("build/reports/cinematic-landscape.png")
    }
}
