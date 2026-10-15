package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.components.ConfettiEffect
import com.example.ui.components.TeamEmblem
import com.example.ui.theme.ModernAmber
import com.example.ui.theme.ModernBg
import com.example.ui.theme.ModernCard
import com.example.ui.theme.ModernCrimson
import com.example.ui.theme.ModernEmerald
import com.example.ui.theme.ModernIndigo
import com.example.ui.theme.ModernIndigoLight
import com.example.ui.theme.ModernSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GameUiState

@Composable
fun WinnerScreen(
    uiState: GameUiState,
    onPlayAgain: () -> Unit,
    onReturnToSetup: () -> Unit
) {
    val isNepali = uiState.appLanguage == AppLanguage.NEPALI
    val team1 = uiState.team1
    val team2 = uiState.team2

    val team1Color = Color(team1.colorHex)
    val team2Color = Color(team2.colorHex)

    val isTie = team1.score == team2.score
    val winnerTeam = when {
        team1.score > team2.score -> team1
        team2.score > team1.score -> team2
        else -> null
    }

    val winnerColor = when {
        winnerTeam?.id == 1 -> team1Color
        winnerTeam?.id == 2 -> team2Color
        else -> ModernIndigo
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ModernBg)
    ) {
        // Confetti celebration animation
        ConfettiEffect()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Trophy and Winner Announcement Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ModernSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = winnerColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isTie) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Tie",
                                    tint = ModernAmber,
                                    modifier = Modifier.size(38.dp)
                                )
                            } else {
                                TeamEmblem(
                                    teamId = winnerTeam?.id ?: 1,
                                    color = winnerColor,
                                    size = 72.dp,
                                    iconSize = 38.dp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Celebratory Banner
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = ModernAmber.copy(alpha = 0.2f),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = if (isNepali) {
                                if (isTie) "खेल बराबरी भयो!" else "विजेता ग्याङ"
                            } else {
                                if (isTie) "MATCH TIED!" else "CHAMPIONS"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = ModernAmber,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isNepali) {
                            if (isTie) "खेल बराबरी भयो!" else "${winnerTeam?.name} ले जित्यो!"
                        } else {
                            if (isTie) "What a battle! It's a draw!" else "${winnerTeam?.name} Won the Match!"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isNepali) {
                            if (isTie) "दुवै ग्याङ उत्तिकै कडा! बराबर ${team1.score} अङ्क!" else "उत्कृष्ट अभिनय र अनुमान! कुल ${winnerTeam?.score} अङ्क सहित विजय!"
                        } else {
                            if (isTie) "Both gangs played fiercely! Equal ${team1.score} points!" else "Victory with ${winnerTeam?.score} points!"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Final Scoreboard Comparison with Gang Colors
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ModernSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Team 1 Stats
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        TeamEmblem(
                            teamId = team1.id,
                            color = team1Color,
                            size = 36.dp,
                            iconSize = 18.dp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = team1.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = team1Color,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        Text(
                            text = if (isNepali) "${team1.score} अङ्क" else "${team1.score} PTS",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = if (isNepali) "सही: ${team1.correctCount} • पास: ${team1.passedCount + team1.timeOutCount}"
                                   else "Correct: ${team1.correctCount} • Missed: ${team1.passedCount + team1.timeOutCount}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(50.dp)
                            .background(ModernCard)
                    )

                    // Team 2 Stats
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        TeamEmblem(
                            teamId = team2.id,
                            color = team2Color,
                            size = 36.dp,
                            iconSize = 18.dp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = team2.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = team2Color,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        Text(
                            text = if (isNepali) "${team2.score} अङ्क" else "${team2.score} PTS",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = if (isNepali) "सही: ${team2.correctCount} • पास: ${team2.passedCount + team2.timeOutCount}"
                                   else "Correct: ${team2.correctCount} • Missed: ${team2.passedCount + team2.timeOutCount}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Match Highlights / History
            if (uiState.roundHistory.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = ModernIndigoLight,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isNepali) "खेल विवरण" else "MATCH ROUNDS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = ModernIndigoLight
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(uiState.roundHistory) { record ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ModernSurface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (record.isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (record.isCorrect) ModernEmerald else ModernCrimson,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isNepali && record.movie.nativeTitle.isNotEmpty()) record.movie.nativeTitle else record.movie.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${record.teamName} • ${record.timeUsedSeconds}s",
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                Text(
                                    text = if (record.isCorrect) (if (isNepali) "+१ अङ्क" else "+1 Pt") else "0 Pt",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (record.isCorrect) ModernEmerald else ModernCrimson
                                )
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onReturnToSetup,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ModernCard,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("new_game_setup_button")
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isNepali) "सेटिङ" else "Setup",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onPlayAgain,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ModernIndigo,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .weight(1.4f)
                        .height(48.dp)
                        .testTag("play_again_button")
                ) {
                    Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isNepali) "फेरि खेलौँ" else "Play Again",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
