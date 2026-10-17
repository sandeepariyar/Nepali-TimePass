package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.example.R
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState

@Composable
fun WinnerScreen(uiState: GameUiState, onPlayAgain: () -> Unit, onReturnToSetup: () -> Unit) {
    val ne = uiState.appLanguage == AppLanguage.NEPALI
    val tie = uiState.team1.score == uiState.team2.score
    val winner = if (tie) null else if (uiState.team1.score > uiState.team2.score) uiState.team1 else uiState.team2
    var credits by rememberSaveable { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().background(ModernBg)) {
        Scaffold(containerColor = Color.Transparent, contentWindowInsets = WindowInsets(0, 0, 0, 0), bottomBar = {
            Surface(color = ModernBg) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    CinemaButton(if (ne) "फेरि खेलौँ" else "Play again", onPlayAgain,
                        Modifier.fillMaxWidth().testTag("play_again_button"))
                    TextButton(onClick = onReturnToSetup, modifier = Modifier.fillMaxWidth().testTag("new_game_setup_button")) {
                        Text(if (ne) "नयाँ सेटअप" else "New setup", color = TextPrimary)
                    }
                }
            }
        }) { padding ->
            Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.fillMaxWidth().height(170.dp)) {
                    Image(painterResource(R.drawable.winner_stage), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent, ModernBg))))
                    if (winner != null) WinnerBadgeBurst(Modifier.size(170.dp).align(Alignment.Center).offset(y = (-8).dp))
                    if (winner != null) Image(painterResource(gangBadge(winner.id)), null,
                        Modifier.size(108.dp).align(Alignment.Center).offset(y = (-8).dp))
                    else Row(Modifier.align(Alignment.Center), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Image(painterResource(gangBadge(1)), null, Modifier.size(76.dp))
                        Image(painterResource(gangBadge(2)), null, Modifier.size(76.dp))
                    }
                }
                if (tie) Text(if (ne) "बराबरी!" else "A PERFECT DRAW!", color = Color(0xFFFFC85B),
                    style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black,
                    modifier = Modifier.testTag("winner_heading"))
                else Image(painterResource(if (ne) R.drawable.winner_title_ne else R.drawable.winner_title_en),
                    null, Modifier.fillMaxWidth(.82f).height(88.dp).testTag("winner_heading").semantics {
                        contentDescription = if (ne) "विजेता!" else "WINNER!"
                        heading()
                    }, contentScale = ContentScale.Fit)
                Text(winner?.name ?: if (ne) "दुवै ग्याङ मञ्चमा छाए!" else "Both gangs stole the show!",
                    Modifier.fillMaxWidth().padding(horizontal = 20.dp).testTag("winning_team_name"),
                    color = TextPrimary, textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                if (!tie) Text(if (ne) "आजको जमघटमा तपाईंहरू छाउनुभयो!" else "That’s a win for the group chat.",
                    color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                Text(if (ne) "खेलको नतिजा • ${uiState.roundHistory.size} पालो" else "Final score • ${uiState.roundHistory.size} turns",
                    color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                if (LocalDensity.current.fontScale > 1.3f) {
                    Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        FinalScoreCard(uiState.team1, winner?.id == 1, ne, Modifier.fillMaxWidth())
                        FinalScoreCard(uiState.team2, winner?.id == 2, ne, Modifier.fillMaxWidth())
                    }
                } else Row(Modifier.padding(horizontal = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FinalScoreCard(uiState.team1, winner?.id == 1, ne, Modifier.weight(1f))
                    FinalScoreCard(uiState.team2, winner?.id == 2, ne, Modifier.weight(1f))
                }
                if (uiState.roundHistory.isNotEmpty()) {
                    OutlinedButton(onClick = { credits = !credits }, modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().testTag("match_details_button")) {
                        Text(if (ne) "खेल विवरण ${if (credits) "▴" else "▾"}" else "Match details ${if (credits) "▴" else "▾"}", color = ModernAmber)
                    }
                    AnimatedVisibility(credits) {
                        Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            uiState.roundHistory.forEach { record ->
                                Text("${if (record.isCorrect) "+1" else "0"} · ${record.teamName}\n${if (ne && record.movie.nativeTitle.isNotBlank()) record.movie.nativeTitle else record.movie.title} · ${record.timeUsedSeconds}s",
                                    color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

private fun gangBadge(id: Int) = if (id == 1) R.drawable.gang_lightning else R.drawable.gang_flame

@Composable
private fun FinalScoreCard(team: Team, winner: Boolean, ne: Boolean, modifier: Modifier) {
    val edge = if (winner) Color(0xFFFFC85B) else Color(team.colorHex).copy(alpha = .65f)
    Surface(modifier.testTag("final_score_${team.id}"), shape = RoundedCornerShape(24.dp),
        color = Color(team.colorHex).copy(alpha = .10f), border = BorderStroke(if (winner) 2.dp else 1.dp, edge)) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            if (winner) Icon(Icons.Default.EmojiEvents, if (ne) "विजेता" else "Winner", tint = edge, modifier = Modifier.size(20.dp))
            else Spacer(Modifier.height(20.dp))
            Image(painterResource(gangBadge(team.id)), null, Modifier.size(52.dp))
            Text(team.name, textAlign = TextAlign.Center, color = TextPrimary, fontWeight = FontWeight.Bold,
                minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
            Text("${team.score}", color = TextPrimary, fontSize = 42.sp, lineHeight = 46.sp,
                fontWeight = FontWeight.Black, modifier = Modifier.testTag("final_points_${team.id}"))
        }
    }
}
