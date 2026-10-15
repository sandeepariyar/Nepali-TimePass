package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState

@Composable
fun WinnerScreen(uiState: GameUiState, onPlayAgain: () -> Unit, onReturnToSetup: () -> Unit) {
    val ne = uiState.appLanguage == AppLanguage.NEPALI
    val winner = if (uiState.team1.score > uiState.team2.score) uiState.team1 else uiState.team2
    val tie = uiState.team1.score == uiState.team2.score
    Box(Modifier.fillMaxSize()) {
    CinemaScene {
        TeamEmblem(winner.id, Color(winner.colorHex), size = 144.dp)
        CinemaHeading(if (ne) "आजको सुपरहिट" else "TONIGHT’S BLOCKBUSTER",
            if (tie) { if (ne) "दुवै ग्याङ विजेता!" else "A glorious draw!" } else winner.name,
            if (ne) "ताली बजाउनुहोस्!" else "Take a bow. You earned it.")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GangScore(uiState.team1, Modifier.weight(1f)); GangScore(uiState.team2, Modifier.weight(1f))
        }
        CinemaButton(if (ne) "फेरि खेलौँ" else "PLAY THE SEQUEL", onPlayAgain, Modifier.fillMaxWidth().testTag("play_again_button"))
        CinemaButton(if (ne) "नयाँ सेटअप" else "New setup", onReturnToSetup, Modifier.fillMaxWidth().testTag("new_game_setup_button"), secondary = true)
        if (uiState.roundHistory.isNotEmpty()) CinemaPanel {
            Text(if (ne) "खेल विवरण" else "THE CREDITS", color = ModernAmber)
            uiState.roundHistory.forEach { record ->
                Text("${if (record.isCorrect) "+1" else "0"}  ${record.teamName} • ${if (ne && record.movie.nativeTitle.isNotBlank()) record.movie.nativeTitle else record.movie.title} • ${record.timeUsedSeconds}s", color = TextPrimary)
            }
        }
    }
    ConfettiEffect(particleCount = 36)
    }
}
