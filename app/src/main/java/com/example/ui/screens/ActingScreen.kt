package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.components.MovieCard
import com.example.ui.theme.ModernAmber
import com.example.ui.theme.ModernBg
import com.example.ui.theme.ModernCard
import com.example.ui.theme.ModernCrimson
import com.example.ui.theme.ModernEmerald
import com.example.ui.theme.ModernIndigo
import com.example.ui.theme.ModernSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GameUiState

@Composable
fun ActingScreen(
    uiState: GameUiState,
    onToggleReveal: () -> Unit,
    onTogglePauseTimer: () -> Unit,
    onGuessCorrect: () -> Unit,
    onGuessWrongOrPass: () -> Unit,
    onSkipMovie: () -> Unit,
    onConcludeGame: () -> Unit
) {
    val isNepali = uiState.appLanguage == AppLanguage.NEPALI
    val activeTeamId = uiState.activeTeamId
    val team1Color = Color(uiState.team1.colorHex)
    val team2Color = Color(uiState.team2.colorHex)

    val minutes = uiState.remainingSeconds / 60
    val seconds = uiState.remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ModernBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP SECTION: BATTLE SCOREBOARD & CENTER TIMER CAPSULE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team 1 Card
                val isTeam1Active = activeTeamId == 1
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isTeam1Active) team1Color.copy(alpha = 0.25f) else ModernSurface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = uiState.team1.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isTeam1Active) team1Color else TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${uiState.team1.score}",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    // ACTIVE floating top pill
                    if (isTeam1Active) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = ModernAmber,
                            modifier = Modifier.align(Alignment.TopCenter)
                        ) {
                            Text(
                                text = if (isNepali) "सक्रिय" else "ACTIVE",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Center Timer Capsule
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ModernSurface,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onTogglePauseTimer() }
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Timer",
                            tint = if (uiState.remainingSeconds <= 15) ModernCrimson else ModernIndigo,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = timeFormatted,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = if (uiState.remainingSeconds <= 15) ModernCrimson else Color.White,
                            letterSpacing = (-0.5).sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Team 2 Card
                val isTeam2Active = activeTeamId == 2
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isTeam2Active) team2Color.copy(alpha = 0.25f) else ModernSurface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = uiState.team2.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isTeam2Active) team2Color else TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${uiState.team2.score}",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    // ACTIVE floating top pill
                    if (isTeam2Active) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = ModernAmber,
                            modifier = Modifier.align(Alignment.TopCenter)
                        ) {
                            Text(
                                text = if (isNepali) "सक्रिय" else "ACTIVE",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // MIDDLE SECTION: FLAT MOVIE CARD
            uiState.currentMovie?.let { movie ->
                MovieCard(
                    movie = movie,
                    isCardRevealed = uiState.isCardRevealed,
                    onToggleReveal = onToggleReveal,
                    onSkip = onSkipMovie,
                    appLanguage = uiState.appLanguage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 12.dp)
                )
            }

            // BOTTOM CONTROLS: PASS AND CORRECT BUTTONS
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // PASS BUTTON
                    Button(
                        onClick = onGuessWrongOrPass,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ModernCard,
                            contentColor = ModernCrimson
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .testTag("guess_wrong_button")
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = ModernCrimson,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isNepali) "छोड्नुहोस्" else "PASS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = ModernCrimson
                            )
                        }
                    }

                    // CORRECT! BUTTON
                    Button(
                        onClick = onGuessCorrect,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ModernEmerald,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .weight(2f)
                            .height(64.dp)
                            .testTag("guess_correct_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isNepali) "मिलेको!" else "CORRECT!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // End Game Early Link
                Text(
                    text = if (isNepali) "खेल समाप्त गरी नतिजा हेर्नुहोस्" else "End Match & View Winner",
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable { onConcludeGame() }
                        .padding(vertical = 4.dp)
                        .testTag("conclude_game_acting_button")
                )
            }
        }
    }
}
