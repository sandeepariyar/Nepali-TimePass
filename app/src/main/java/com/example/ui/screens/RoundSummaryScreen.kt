package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState

@Composable
fun RoundSummaryScreen(uiState: GameUiState, onNextTurn: () -> Unit, onConcludeGame: () -> Unit) {
    val ne = uiState.appLanguage == AppLanguage.NEPALI
    val record = uiState.lastRoundResult
    val correct = record?.isCorrect == true
    val timeout = uiState.remainingSeconds == 0
    CinemaScene {
        CinemaHeading(if (ne) "राउन्ड ${uiState.currentRoundNumber} • नतिजा" else "ROUND ${uiState.currentRoundNumber} • CUT!",
            if (correct) { if (ne) "वाह! मिल्यो!" else "What a performance!" }
            else if (timeout) { if (ne) "समय सकियो!" else "That’s time!" }
            else { if (ne) "अर्को पटक मिल्छ!" else "Save it for the sequel." },
            if (correct) { if (ne) "+१ अङ्क" else "+1 POINT" } else { if (ne) "यो पालोमा ० अङ्क" else "0 POINTS THIS TURN" })
        record?.let {
            CinemaPanel {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    MoviePosterBadge(it.movie, width = 84.dp, height = 126.dp)
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (ne && it.movie.nativeTitle.isNotBlank()) it.movie.nativeTitle else it.movie.title, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                        Text("${it.movie.year} • ${it.movie.genre}", color = TextSecondary)
                        Text("${it.teamName} • ${it.timeUsedSeconds}s", color = ModernAmber)
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GangScore(uiState.team1, Modifier.weight(1f)); GangScore(uiState.team2, Modifier.weight(1f))
        }
        CinemaButton(if (ne) "अर्को ग्याङको पालो" else "NEXT GANG, TAKE THE STAGE", onNextTurn, Modifier.fillMaxWidth().testTag("next_turn_button"))
        CinemaButton(if (ne) "खेलको नतिजा" else "Finish match", onConcludeGame, Modifier.fillMaxWidth().testTag("conclude_game_button"), secondary = true)
    }
}
