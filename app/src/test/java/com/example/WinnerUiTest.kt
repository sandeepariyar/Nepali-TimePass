package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.model.*
import com.example.ui.screens.WinnerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameUiState
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.*

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h640dp-xxhdpi")
class WinnerUiTest {
    @get:Rule val compose = createComposeRule()
    @Test fun teamTwoWinsWithRealScoresAndPinnedReplay() {
        var played = 0
        compose.setContent { MyApplicationTheme {
            WinnerScreen(GameUiState(appLanguage = AppLanguage.ENGLISH,
                team1 = Team(1, "Momo Gang", 0xFFE11D48, "", score = 2),
                team2 = Team(2, "Kathmandu Crew", 0xFF2563EB, "", score = 7)), { played++ }, {})
        } }
        compose.onNodeWithTag("winning_team_name").assertTextEquals("Kathmandu Crew")
        compose.onNodeWithTag("final_points_2").performScrollTo().assertTextEquals("7")
        compose.onNodeWithTag("play_again_button").assertIsDisplayed().performClick()
        assertEquals(1, played)
        compose.onRoot().captureRoboImage("build/reports/winner-team-two.png")
    }
    @Test fun drawDoesNotDeclareEitherGangTheWinner() {
        compose.setContent { MyApplicationTheme {
            WinnerScreen(GameUiState(team1 = Team(1, "पहिलो ग्याङ", 0xFFE11D48, "", score = 3),
                team2 = Team(2, "दोस्रो ग्याङ", 0xFF2563EB, "", score = 3)), {}, {})
        } }
        compose.onNodeWithTag("winner_heading").assertTextEquals("बराबरी!")
        compose.onNodeWithContentDescription("विजेता").assertDoesNotExist()
        compose.onNodeWithTag("new_game_setup_button").assertIsDisplayed()
    }
    @Test fun fullMatchDetailsExpandWithoutChangingScore() {
        val movie = Movie("qa-details", "Test Film", "परीक्षण", 2026, MovieLanguage.NEPALI, "Drama")
        compose.setContent { MyApplicationTheme {
            WinnerScreen(GameUiState(appLanguage = AppLanguage.ENGLISH,
                roundHistory = listOf(RoundRecord(1, "Momo Gang", 0xFFE11D48, movie, true, 12))), {}, {})
        } }
        compose.onNodeWithText("Test Film", substring = true).assertDoesNotExist()
        compose.onNodeWithTag("match_details_button").performScrollTo().performClick()
        compose.onNodeWithText("Test Film", substring = true).assertExists()
        compose.onNodeWithTag("play_again_button").assertIsDisplayed()
    }
}
