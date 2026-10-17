package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.model.*
import com.example.ui.screens.ReadyTurnScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameUiState
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.*

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h640dp-xxhdpi")
class ReadyTurnUiTest {
    @get:Rule val compose = createComposeRule()
    @Test fun activeGangHasLargeBadgeAndOneTapStart() {
        var starts = 0
        compose.setContent { MyApplicationTheme {
            ReadyTurnScreen(GameUiState(appLanguage = AppLanguage.ENGLISH, activeTeamId = 2,
                team1 = Team(1, "Momo Gang", 0xFFE11D48, "", score = 2),
                team2 = Team(2, "Kathmandu Crew", 0xFF2563EB, "", score = 3)), { starts++ }, {})
        } }
        compose.onNodeWithTag("active_gang_badge_2").assertIsDisplayed()
        compose.onNodeWithTag("active_gang_name").assertTextEquals("Kathmandu Crew")
        compose.onNodeWithTag("start_turn_button").assertIsDisplayed().performClick()
        assertEquals(1, starts)
        compose.onRoot().captureRoboImage("build/reports/ready-active-gang.png")
    }
}
