package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState

@Composable
fun ReadyTurnScreen(uiState: GameUiState, onStartTurn: () -> Unit, onConcludeGame: () -> Unit, onBackToSetup: () -> Unit = {}) {
    val ne = uiState.appLanguage == AppLanguage.NEPALI
    val gang = if (uiState.activeTeamId == 1) uiState.team1 else uiState.team2
    CinemaScene {
        CinemaHeading(if (ne) "राउन्ड ${uiState.currentRoundNumber}" else "ROUND ${uiState.currentRoundNumber}",
            if (ne) "मञ्च तपाईंको हो!" else "The stage is yours.", gang.name)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GangScore(uiState.team1, Modifier.weight(1f), uiState.activeTeamId == 1)
            GangScore(uiState.team2, Modifier.weight(1f), uiState.activeTeamId == 2)
        }
        if (uiState.roundInterrupted) Text(if (ne) "फोन फेरि सुरु भएकाले अधुरो पालो पुनः खेल्नुहोस्।" else "The device restarted. Replay the interrupted turn.", color = ModernAmber)
        CinemaPanel {
            Text(if (ne) "फोन कलाकारलाई दिनुहोस्" else "Pass the phone to the actor", color = ModernAmber)
            Text(if (ne) "फिल्म गोप्य राख्नुहोस्। नबोली अभिनय गर्नुहोस्। ${uiState.settings.timerDurationSeconds} सेकेन्डमा अनुमान मिलाउनुहोस्।" else
                "Keep the film secret. Act without speaking. Your gang has ${uiState.settings.timerDurationSeconds} seconds to guess.", color = TextPrimary)
        }
        CinemaButton(if (ne) "बत्ती, क्यामेरा, अभिनय!" else "LIGHTS. CAMERA. ACTION!", onStartTurn, Modifier.fillMaxWidth().testTag("start_turn_button"))
        CinemaButton(if (ne) "विजेता हेर्नुहोस्" else "Finish match", onConcludeGame, Modifier.fillMaxWidth(), secondary = true)
        CinemaButton(if (ne) "सेटअपमा फर्कनुहोस्" else "Back to setup", onBackToSetup, Modifier.fillMaxWidth().testTag("back_to_setup_button"), secondary = true)
    }
}
