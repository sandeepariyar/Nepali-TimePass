package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.model.*
import com.example.ui.screens.*
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
class CinematicScreensTest {
    @get:Rule val compose = createComposeRule()
    @Test fun welcomeScreenshot() {
        compose.setContent { MyApplicationTheme { SplashScreen {} } }
        compose.onNodeWithTag("splash_continue_button").assertIsDisplayed()
        compose.onRoot().captureRoboImage("build/reports/cinematic-welcome.png")
    }
    @Test fun setupAndCategoryScreenshots() {
        val app = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm = com.example.viewmodel.GameViewModel(app, false)
        vm.dismissSplash()
        compose.setContent { MyApplicationTheme { GameAppContent(vm) } }
        compose.onNodeWithTag("start_game_button").assertIsDisplayed()
        compose.onRoot().captureRoboImage("build/reports/cinematic-setup.png")
        compose.onNodeWithTag("cinema_section").let { if (!it.isDisplayed()) it.performScrollTo() }
        compose.onNodeWithTag("category_NEPALI").assertIsDisplayed()
        compose.onRoot().captureRoboImage("build/reports/cinematic-categories.png")
        vm.returnToSetup()
    }
    @Test fun handoffScreenshot() {
        compose.setContent { MyApplicationTheme { ReadyTurnScreen(GameUiState(), {}, {}, {}) } }
        compose.onRoot().captureRoboImage("build/reports/cinematic-ready.png")
    }
    @Test fun winnerScreenshot() {
        compose.setContent { MyApplicationTheme { WinnerScreen(GameUiState(team1 = Team(1, "काठमाडौँका कलाकार", 0xFFE11D48, "", score = 5), team2 = Team(2, "पोखराका हिरो", 0xFF2563EB, "", score = 3)), {}, {}) } }
        compose.mainClock.advanceTimeBy(1600)
        compose.onRoot().captureRoboImage("build/reports/cinematic-winner.png")
    }
}
