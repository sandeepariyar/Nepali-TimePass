package com.example

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import android.app.Application
import com.example.model.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameViewModel
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.*

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h640dp-xxhdpi")
class HomeSetupTest {
    @get:Rule val compose = createComposeRule()
    private lateinit var vm: GameViewModel
    @Before fun setup() {
        vm = GameViewModel(ApplicationProvider.getApplicationContext<Application>(), false)
        vm.dismissSplash()
        vm.setAppLanguage(AppLanguage.ENGLISH)
    }
    @After fun cleanup() { vm.returnToSetup() }
    private fun render(largeText: Boolean = false) {
        compose.setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, if (largeText) 1.8f else 1f)) {
                MyApplicationTheme { GameAppContent(vm) }
            }
        }
    }
    @Test fun categoryDurationAndStickyStartUseExistingGameState() {
        render()
        compose.onRoot().captureRoboImage("build/reports/package-home-compact-english.png")
        compose.onNodeWithTag("cinema_section").let { if (!it.isDisplayed()) it.performScrollTo() }
        compose.onNodeWithTag("team_1_card").assertIsDisplayed()
        compose.onNodeWithTag("team_2_card").assertIsDisplayed()
        compose.onNodeWithTag("duration_menu").assertIsDisplayed()
        listOf("NEPALI", "HINDI_MODERN", "HINDI_CLASSIC", "SOUTH_INDIAN").forEach {
            compose.onNodeWithTag("category_$it").assertIsDisplayed()
        }
        compose.onNodeWithTag("category_NEPALI").assertIsOn().performClick().assertIsOff()
        assertFalse(MovieLanguage.NEPALI in vm.uiState.value.settings.selectedLanguages)
        compose.onNodeWithTag("duration_menu").performClick()
        compose.onNodeWithTag("duration_90").performClick()
        assertEquals(90, vm.uiState.value.settings.timerDurationSeconds)
        compose.onNodeWithTag("start_game_button").assertIsDisplayed().performClick()
        assertEquals(GamePhase.READY_TURN, vm.uiState.value.phase)
    }
    @Test fun editableGangNamesAndBothLanguagesRemainAvailable() {
        render()
        compose.onNodeWithTag("team_1_card").performClick()
        compose.onNodeWithTag("team_1_name").performTextReplacement("Kathmandu Crew")
        compose.runOnIdle { assertEquals("Kathmandu Crew", vm.uiState.value.team1.name) }
        compose.onNodeWithTag("gang_editor_done").performClick()
        compose.onNodeWithTag("setup_language_NEPALI").performClick()
        compose.runOnIdle { assertEquals(AppLanguage.NEPALI, vm.uiState.value.appLanguage) }
        compose.onNodeWithTag("start_game_button").assertIsDisplayed()
        compose.onRoot().captureRoboImage("build/reports/package-home-nepali.png")
    }
    @Test fun filmLibraryAndCustomFilmDialogRemainReachable() {
        render()
        compose.onNodeWithTag("film_library_button").performClick()
        compose.onNodeWithText("Search online films").assertIsDisplayed()
        compose.onNodeWithTag("add_custom_movie_button").performScrollTo().performClick()
        compose.onNodeWithText("Movie Title (English)").assertExists()
    }
    @Test fun largeTextKeepsStartVisibleWhileTeamsStack() {
        render(true)
        compose.onNodeWithTag("start_game_button").assertIsDisplayed()
        compose.onNodeWithTag("team_2_card").let { if (!it.isDisplayed()) it.performScrollTo(); it.assertIsDisplayed() }
        compose.onNodeWithTag("start_game_button").assertIsDisplayed()
        compose.onRoot().captureRoboImage("build/reports/package-home-large-text.png")
    }
    @Test @Config(qualifiers = "w640dp-h360dp-land-xxhdpi")
    fun landscapeKeepsStartVisible() {
        render()
        compose.onNodeWithTag("start_game_button").assertIsDisplayed()
        compose.onNodeWithTag("team_2_card").let { if (!it.isDisplayed()) it.performScrollTo(); it.assertIsDisplayed() }
        compose.onRoot().captureRoboImage("build/reports/package-home-landscape.png")
    }
}
