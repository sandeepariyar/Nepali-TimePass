package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    CinemaBackdrop {
      Scaffold(containerColor = ModernBg, contentWindowInsets = WindowInsets(0, 0, 0, 0), bottomBar = {
        Column(Modifier.widthIn(max = 600.dp).fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
          CinemaButton(if (ne) "फिल्म हेरेर सुरु गरौँ" else "Reveal & start", onStartTurn,
            Modifier.fillMaxWidth().testTag("start_turn_button"))
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onConcludeGame) { Text(if (ne) "विजेता हेर्नुहोस्" else "Finish match", color = TextSecondary) }
            TextButton(onClick = onBackToSetup, modifier = Modifier.testTag("back_to_setup_button")) {
              Text(if (ne) "सेटअपमा फर्कनुहोस्" else "Back to setup", color = TextSecondary)
            }
          }
        }
      }) { insets ->
       Column(Modifier.padding(insets).widthIn(max = 600.dp).fillMaxSize()
          .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        uiState.lastRoundResult?.let { result ->
            CinemaPanel {
                Text(if (result.isCorrect) { if (ne) "मिल्यो! +१ अङ्क" else "Correct! +1 point" }
                    else if (result.timeUsedSeconds >= uiState.settings.timerDurationSeconds) { if (ne) "समय सकियो" else "Time’s up" }
                    else { if (ne) "पास गरियो" else "Passed" }, color = if (result.isCorrect) ModernAmber else TextSecondary)
                Text("${result.teamName} · ${if (ne && result.movie.nativeTitle.isNotBlank()) result.movie.nativeTitle else result.movie.title}", color = TextPrimary)
            }
        }
        Text(if (ne) "राउन्ड ${uiState.currentRoundNumber}" else "ROUND ${uiState.currentRoundNumber}",
            color = ModernAmber, style = MaterialTheme.typography.labelLarge)
        Text(if (ne) "अब तपाईंको पालो" else "You’re up next.", color = TextPrimary,
            style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        TeamEmblem(gang.id, Color(gang.colorHex), Modifier.testTag("active_gang_badge_${gang.id}"), size = 136.dp)
        Text(gang.name, Modifier.testTag("active_gang_name"), color = TextPrimary,
            style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(uiState.team1, uiState.team2).forEach { team ->
                Surface(Modifier.weight(1f), color = if (team.id == gang.id) Color(team.colorHex).copy(alpha = .16f) else ModernSurface,
                    shape = RoundedCornerShape(20.dp)) {
                    Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TeamEmblem(team.id, Color(team.colorHex), size = 32.dp)
                        Text(team.name, Modifier.weight(1f), color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                        Text("${team.score}", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        if (uiState.roundInterrupted) Text(if (ne) "फोन फेरि सुरु भएकाले अधुरो पालो पुनः खेल्नुहोस्।" else "The device restarted. Replay the interrupted turn.", color = ModernAmber)
        CinemaPanel {
            Text(if (ne) "फोन कलाकारलाई दिनुहोस्" else "Pass the phone to the actor", color = ModernAmber)
            Text(if (ne) "फिल्म गोप्य राख्नुहोस्। नबोली अभिनय गर्नुहोस्। ${uiState.settings.timerDurationSeconds} सेकेन्डमा अनुमान मिलाउनुहोस्।" else
                "Keep the film secret. Act without speaking. Your gang has ${uiState.settings.timerDurationSeconds} seconds to guess.", color = TextPrimary)
        }
       }
      }
    }
}
