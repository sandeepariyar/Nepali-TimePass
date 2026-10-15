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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Leaderboard
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
import com.example.ui.components.MoviePosterBadge
import com.example.ui.components.TeamEmblem
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
fun RoundSummaryScreen(
    uiState: GameUiState,
    onNextTurn: () -> Unit,
    onConcludeGame: () -> Unit
) {
    val isNepali = uiState.appLanguage == AppLanguage.NEPALI
    val lastResult = uiState.lastRoundResult
    val isCorrect = lastResult?.isCorrect == true
    val team1Color = Color(uiState.team1.colorHex)
    val team2Color = Color(uiState.team2.colorHex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ModernBg)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Result Banner Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isCorrect) ModernEmerald.copy(alpha = 0.15f) else ModernCrimson.copy(alpha = 0.15f)
            ),
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
                    color = if (isCorrect) ModernEmerald else ModernCrimson,
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isCorrect) {
                        if (isNepali) "सहि जवाफ!" else "CORRECT GUESS!"
                    } else {
                        if (isNepali) "समय सकियो / छुट्यो!" else "TIME'S UP / MISSED!"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isCorrect) ModernEmerald else ModernCrimson,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isCorrect) {
                        if (isNepali) "+१ अङ्क ${lastResult?.teamName} लाई थपियो" else "+1 Point added to ${lastResult?.teamName}"
                    } else {
                        if (isNepali) "यस राउन्डमा ० अङ्क" else "0 Points for this round"
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Movie Reveal info
        lastResult?.movie?.let { movie ->
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MoviePosterBadge(
                        movie = movie,
                        width = 72.dp,
                        height = 104.dp,
                        cornerRadius = 10.dp
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isNepali) "फिल्म थियो:" else "Movie was:",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isNepali && movie.nativeTitle.isNotEmpty()) movie.nativeTitle else movie.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${movie.year} • ${movie.genre}",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // Both Group Numbers & Scores side-by-side
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ModernSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Leaderboard,
                        contentDescription = null,
                        tint = ModernIndigoLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isNepali) "हालको अङ्क तालिका" else "CURRENT SCOREBOARD",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = ModernIndigoLight
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Team 1
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        TeamEmblem(
                            teamId = 1,
                            color = team1Color,
                            size = 36.dp,
                            iconSize = 18.dp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.team1.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = team1Color
                        )
                        Text(
                            text = if (isNepali) "${uiState.team1.score} अंक" else "${uiState.team1.score} Pts",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "VS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMuted
                    )

                    // Team 2
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        TeamEmblem(
                            teamId = 2,
                            color = team2Color,
                            size = 36.dp,
                            iconSize = 18.dp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.team2.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = team2Color
                        )
                        Text(
                            text = if (isNepali) "${uiState.team2.score} अंक" else "${uiState.team2.score} Pts",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onNextTurn,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ModernIndigo,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("next_turn_button")
            ) {
                Text(
                    text = if (isNepali) "अर्को पालो" else "NEXT TURN",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onConcludeGame,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ModernCard,
                    contentColor = TextSecondary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("conclude_game_summary_button")
            ) {
                Text(
                    text = if (isNepali) "खेल समाप्त गरी नतिजा हेर्नुहोस्" else "Conclude Match & See Winner",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
