package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Team
import com.example.ui.components.TeamEmblem
import com.example.ui.theme.ModernAmber
import com.example.ui.theme.ModernBg
import com.example.ui.theme.ModernCard
import com.example.ui.theme.ModernCrimson
import com.example.ui.theme.ModernIndigo
import com.example.ui.theme.ModernIndigoLight
import com.example.ui.theme.ModernSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GameUiState

@Composable
fun ReadyTurnScreen(
    uiState: GameUiState,
    onStartTurn: () -> Unit,
    onConcludeGame: () -> Unit,
    onBackToSetup: () -> Unit = {}
) {
    val isNepali = uiState.appLanguage == AppLanguage.NEPALI
    val activeTeam = if (uiState.activeTeamId == 1) uiState.team1 else uiState.team2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ModernBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP BAR: Back Button & Round Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle Back Button
                Surface(
                    shape = CircleShape,
                    color = ModernSurface,
                    modifier = Modifier.size(44.dp)
                ) {
                    IconButton(
                        onClick = onBackToSetup,
                        modifier = Modifier.testTag("back_to_setup_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Setup",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Round Number Capsule
                Surface(
                    shape = RoundedCornerShape(50),
                    color = ModernSurface
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isNepali) "राउन्ड ${uiState.currentRoundNumber}" else "ROUND ${uiState.currentRoundNumber}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = ModernIndigoLight,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.size(44.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CENTERPIECE: Clean Minimalist Typography Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = if (isNepali) "तयार हुनुहुन्छ?" else "GET READY",
                    fontSize = if (isNepali) 32.sp else 34.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isNepali)
                        "${activeTeam.name} को पालो • अभिनय सुरु गरौँ!"
                    else
                        "${activeTeam.name.uppercase()} • TIME TO ACT",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (uiState.activeTeamId == 1) ModernCrimson else ModernIndigoLight,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TEAM FACE-OFF CONTAINER (VS CARDS)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT CARD: Team 1
                    FaceOffTeamCard(
                        team = uiState.team1,
                        isActiveTurn = uiState.activeTeamId == 1,
                        themeColor = Color(uiState.team1.colorHex),
                        isNepali = isNepali,
                        modifier = Modifier.weight(1f)
                    )

                    // RIGHT CARD: Team 2
                    FaceOffTeamCard(
                        team = uiState.team2,
                        isActiveTurn = uiState.activeTeamId == 2,
                        themeColor = Color(uiState.team2.colorHex),
                        isNepali = isNepali,
                        modifier = Modifier.weight(1f)
                    )
                }

                // CENTER VS BADGE
                Surface(
                    shape = CircleShape,
                    color = ModernCard,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "VS",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ACTOR HANDOFF & GAMEPLAY HINT CARD
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ModernSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = ModernAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNepali)
                                "फोन कलाकारलाई दिनुहोस् (${uiState.settings.timerDurationSeconds} सेकेन्ड)"
                            else
                                "Pass phone to ACTOR (${uiState.settings.timerDurationSeconds}s timer)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isNepali)
                            "• नबोली इशाराले मात्र अभिनय गर्नुहोस् • ग्याङले अनुमान गर्नुपर्नेछ"
                        else
                            "• Act with gestures only • No talking or mouthing words!",
                        fontSize = 11.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTTOM ACTION BUTTONS
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onStartTurn,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ModernIndigo,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("start_turn_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isNepali) "सुरु गर्नुहोस्" else "START NOW",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                if (uiState.roundHistory.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isNepali) "खेल समाप्त गरी नतिजा हेर्नुहोस्" else "Conclude Match & View Scores",
                        fontSize = 11.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onConcludeGame)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("conclude_game_button")
                    )
                }
            }
        }
    }
}

@Composable
private fun FaceOffTeamCard(
    team: Team,
    isActiveTurn: Boolean,
    themeColor: Color,
    isNepali: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isActiveTurn) ModernCard else ModernSurface,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 12.dp)
        ) {
            // Active Turn Badge
            if (isActiveTurn) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ModernAmber,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = if (isNepali) "पालो आयो" else "ACTING NOW",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }

            TeamEmblem(
                teamId = team.id,
                color = themeColor,
                size = 52.dp,
                iconSize = 26.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Gang Name
            Text(
                text = team.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Score Pill
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.Black.copy(alpha = 0.3f)
            ) {
                Text(
                    text = if (isNepali) "${team.score} अङ्क" else "${team.score} PTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = themeColor,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }
    }
}
