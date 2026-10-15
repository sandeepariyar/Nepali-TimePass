package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.GangColorPalette
import com.example.model.Movie
import com.example.model.MovieLanguage
import com.example.ui.components.TeamEmblem
import com.example.ui.theme.ArenaBgLight
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.ModernAmber
import com.example.ui.theme.ModernBg
import com.example.ui.theme.ModernBorder
import com.example.ui.theme.ModernBorderStrong
import com.example.ui.theme.ModernCard
import com.example.ui.theme.ModernCardAlt
import com.example.ui.theme.ModernCrimson
import com.example.ui.theme.ModernEmerald
import com.example.ui.theme.ModernIndigo
import com.example.ui.theme.ModernIndigoDeep
import com.example.ui.theme.ModernIndigoLight
import com.example.ui.theme.ModernSurface
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GameUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetupScreen(
    uiState: GameUiState,
    onToggleAppLanguage: () -> Unit = {},
    onSetAppLanguage: (AppLanguage) -> Unit = {},
    onTeam1NameChange: (String) -> Unit,
    onTeam2NameChange: (String) -> Unit,
    onTeam1ColorChange: (Long) -> Unit,
    onTeam2ColorChange: (Long) -> Unit,
    onRandomizeGangs: () -> Unit,
    onRandomizeTeam1: () -> Unit = {},
    onRandomizeTeam2: () -> Unit = {},
    onTimerDurationChange: (Int) -> Unit,
    onToggleLanguage: (MovieLanguage) -> Unit,
    onSetLanguagePreset: (Set<MovieLanguage>) -> Unit,
    onToggleSound: () -> Unit,
    onStartGame: () -> Unit,
    onFetchMoreOnline: () -> Unit = {},
    onSearchOnlineAndAdd: (String) -> Unit = {},
    onAddCustomMovie: (Movie) -> Unit
) {
    val isNepali = uiState.appLanguage == AppLanguage.NEPALI
    var showRulesDialog by remember { mutableStateOf(false) }
    var showAddMovieDialog by remember { mutableStateOf(false) }

    val team1Color = Color(uiState.team1.colorHex)
    val team2Color = Color(uiState.team2.colorHex)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_vs")
    val vsScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vs_scale"
    )

    if (showRulesDialog) {
        RulesDialog(onDismiss = { showRulesDialog = false })
    }

    if (showAddMovieDialog) {
        AddCustomMovieDialog(
            onDismiss = { showAddMovieDialog = false },
            onAddCustomMovie = onAddCustomMovie
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ModernBg)
    ) {
        // Deep background glowing atmospheric orbs
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.TopStart)
                .background(
                    Brush.radialGradient(
                        colors = listOf(NeonPurple.copy(alpha = 0.22f), Color.Transparent)
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.CenterEnd)
                .background(
                    Brush.radialGradient(
                        colors = listOf(NeonBlue.copy(alpha = 0.18f), Color.Transparent)
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomStart)
                .background(
                    Brush.radialGradient(
                        colors = listOf(NeonPink.copy(alpha = 0.18f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HEADER BAR SECTION
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = ModernSurface,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Bar (Settings, ENG/NEP Toggle capsule, Info button)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ModernCard,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { showRulesDialog = true }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // ENG | NEP Language Toggle Capsule
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = ModernCard,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // ENG option
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = if (!isNepali) ModernIndigo else Color.Transparent,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable { onSetAppLanguage(AppLanguage.ENGLISH) }
                                ) {
                                    Text(
                                        text = "ENG",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (!isNepali) Color.White else TextMuted,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                    )
                                }

                                // NEP option
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = if (isNepali) ModernIndigo else Color.Transparent,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable { onSetAppLanguage(AppLanguage.NEPALI) }
                                ) {
                                    Text(
                                        text = "NEP",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isNepali) Color.White else TextMuted,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }

                        // Info / Rules
                        Surface(
                            shape = CircleShape,
                            color = ModernCard,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { showRulesDialog = true }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.HelpOutline,
                                    contentDescription = "Rules & Info",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = if (isNepali) "नेपाली टाइमपास" else "Nepali Timepass",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-0.5).sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isNepali) "दम सिराड्स पार्टी खेल" else "Dumb Charades Party Game",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ModernIndigoLight,
                        letterSpacing = 1.2.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Movies Stats Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ModernCard,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = ModernIndigo,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Movie,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = if (isNepali) "${uiState.totalMoviesCount}+ चलचित्रहरू" else "${uiState.totalMoviesCount}+ Movies",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                    Text(
                                        text = if (isNepali) "खेल्न तयार" else "READY TO PLAY",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ModernEmerald,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = ModernCardAlt,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onFetchMoreOnline() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudUpload,
                                        contentDescription = null,
                                        tint = ModernEmerald,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isNepali) "अपडेट" else "Update",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 1: PLAYERS / GANGS - GAMIFIED VS CARDS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = ModernIndigoLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isNepali) "खेलाडी ग्याङहरू" else "PLAYERS & GANGS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ModernCard,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onRandomizeGangs() }
                            .testTag("randomize_gangs_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = ModernIndigoLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isNepali) "फेरबदल" else "Shuffle",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Dual Gang VS Container with floating VS badge
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Gang 1 (Team 1)
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(200.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ModernSurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                TeamEmblem(
                                    teamId = 1,
                                    color = team1Color,
                                    size = 54.dp,
                                    iconSize = 26.dp
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = uiState.team1.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                }

                                // Score Bottom Bar
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(ModernCard, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isNepali) "अङ्क" else "SCORE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = "${uiState.team1.score}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = team1Color
                                    )
                                }
                            }
                        }

                        // Gang 2 (Team 2)
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(200.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ModernSurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                TeamEmblem(
                                    teamId = 2,
                                    color = team2Color,
                                    size = 54.dp,
                                    iconSize = 26.dp
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = uiState.team2.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                }

                                // Score Bottom Bar
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(ModernCard, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isNepali) "अङ्क" else "SCORE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = "${uiState.team2.score}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = team2Color
                                    )
                                }
                            }
                        }
                    }

                    // VS Badge
                    Surface(
                        shape = CircleShape,
                        color = ModernIndigo,
                        modifier = Modifier
                            .scale(vsScale)
                            .size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "VS",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic,
                                color = Color.White,
                                letterSpacing = (-0.5).sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Editable name input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.team1.name,
                        onValueChange = onTeam1NameChange,
                        singleLine = true,
                        placeholder = { Text(if (isNepali) "ग्याङ १ को नाम" else "Gang 1 Name", fontSize = 11.sp, color = TextMuted) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("team1_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = team1Color,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = ModernCard,
                            unfocusedContainerColor = ModernCard
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = uiState.team2.name,
                        onValueChange = onTeam2NameChange,
                        singleLine = true,
                        placeholder = { Text(if (isNepali) "ग्याङ २ को नाम" else "Gang 2 Name", fontSize = 11.sp, color = TextMuted) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("team2_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = team2Color,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = ModernCard,
                            unfocusedContainerColor = ModernCard
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 2: ARENAS / MOVIE CATEGORIES
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = null,
                                tint = ModernIndigoLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isNepali) "चलचित्र विधा छनोट" else "ARENAS & GENRES",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isNepali) "खेल्नका लागि विधा रोज्नुहोस्" else "Select categories to play",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    val selectedCount = uiState.settings.selectedLanguages.size
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = ModernCard
                    ) {
                        Text(
                            text = if (isNepali) "$selectedCount विधा छानियो" else "$selectedCount SELECTED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = ModernEmerald,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Presets Bar (Horizontal scrolling flat pills)
                val isAllMix = uiState.settings.selectedLanguages.contains(MovieLanguage.NEPALI) &&
                        uiState.settings.selectedLanguages.contains(MovieLanguage.HINDI_MODERN) &&
                        uiState.settings.selectedLanguages.contains(MovieLanguage.SOUTH_INDIAN)
                val isNepaliBollywood = uiState.settings.selectedLanguages == setOf(MovieLanguage.NEPALI, MovieLanguage.HINDI_MODERN)
                val isNepaliOnly = uiState.settings.selectedLanguages == setOf(MovieLanguage.NEPALI)
                val isBollywoodOnly = uiState.settings.selectedLanguages == setOf(MovieLanguage.HINDI_MODERN)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // All Mix
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isAllMix) ModernIndigo else ModernCard,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                onSetLanguagePreset(
                                    setOf(
                                        MovieLanguage.NEPALI,
                                        MovieLanguage.HINDI_MODERN,
                                        MovieLanguage.SOUTH_INDIAN
                                    )
                                )
                            }
                    ) {
                        Text(
                            text = if (isNepali) "सबै सिनेमा" else "All Mix",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAllMix) Color.White else TextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }

                    // Nepali + Bollywood
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isNepaliBollywood) ModernIndigo else ModernCard,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                onSetLanguagePreset(
                                    setOf(
                                        MovieLanguage.NEPALI,
                                        MovieLanguage.HINDI_MODERN
                                    )
                                )
                            }
                    ) {
                        Text(
                            text = if (isNepali) "नेपाल + बलिउड" else "Nepali + Bollywood",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isNepaliBollywood) Color.White else TextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }

                    // Nepali Only
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isNepaliOnly) ModernIndigo else ModernCard,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { onSetLanguagePreset(setOf(MovieLanguage.NEPALI)) }
                    ) {
                        Text(
                            text = if (isNepali) "नेपाली मात्र" else "Nepali Only",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isNepaliOnly) Color.White else TextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }

                    // Bollywood Only
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isBollywoodOnly) ModernIndigo else ModernCard,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { onSetLanguagePreset(setOf(MovieLanguage.HINDI_MODERN)) }
                    ) {
                        Text(
                            text = if (isNepali) "बलिउड मात्र" else "Bollywood Only",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isBollywoodOnly) Color.White else TextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ARENA HERO CARDS WITH CINEMATIC ILLUSTRATION BACKGROUNDS
                val isNepaliSelected = uiState.settings.selectedLanguages.contains(MovieLanguage.NEPALI)
                ArenaHeroCard(
                    title = if (isNepali) "नेपाली चलचित्र" else "Nepali Cinema",
                    subtitle = "Loot, Kabaddi, Jatra, Chhakka Panja...",
                    xpBadge = "+150 XP",
                    difficultyBadge = if (isNepali) "कडा" else "HARD",
                    difficultyColor = ModernCrimson,
                    drawableRes = R.drawable.nepali_cinema_hero_1788199286988,
                    accentColor = ModernIndigo,
                    isSelected = isNepaliSelected,
                    onClick = { onToggleLanguage(MovieLanguage.NEPALI) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                val isHindiSelected = uiState.settings.selectedLanguages.contains(MovieLanguage.HINDI_MODERN)
                ArenaHeroCard(
                    title = if (isNepali) "बलिउड ब्लकबस्टर" else "Bollywood Blockbuster",
                    subtitle = "3 Idiots, Sholay, Dangal, Stree 2...",
                    xpBadge = "+100 XP",
                    difficultyBadge = if (isNepali) "मध्यम" else "MEDIUM",
                    difficultyColor = ModernAmber,
                    drawableRes = R.drawable.bollywood_cinema_hero_1788199301665,
                    accentColor = ModernIndigo,
                    isSelected = isHindiSelected,
                    onClick = { onToggleLanguage(MovieLanguage.HINDI_MODERN) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                val isSouthSelected = uiState.settings.selectedLanguages.contains(MovieLanguage.SOUTH_INDIAN)
                ArenaHeroCard(
                    title = if (isNepali) "साउथ सिनेमा" else "South Cinema",
                    subtitle = "RRR, KGF, Pushpa, Baahubali, Leo...",
                    xpBadge = "+200 XP",
                    difficultyBadge = if (isNepali) "रोमान्चक" else "EXTREME",
                    difficultyColor = ModernCrimson,
                    drawableRes = R.drawable.south_cinema_hero_1788199315366,
                    accentColor = ModernIndigo,
                    isSelected = isSouthSelected,
                    onClick = { onToggleLanguage(MovieLanguage.SOUTH_INDIAN) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Add Custom Arena Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ModernCard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { showAddMovieDialog = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = ModernIndigoLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isNepali) "आफ्नै चलचित्र थप्नुहोस्" else "Add Custom Movie",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 3: TIME LIMIT
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ModernSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = ModernIndigoLight,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isNepali) "समय सीमा" else "TIME LIMIT",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            val mins = uiState.settings.timerDurationSeconds / 60
                            val secs = uiState.settings.timerDurationSeconds % 60
                            val timeDisplay = if (mins > 0 && secs == 0) "$mins:00" else if (mins > 0) "$mins:${if (secs < 10) "0$secs" else "$secs"}" else "0:${if (secs < 10) "0$secs" else "$secs"}"
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = ModernCard
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = ModernIndigoLight,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = timeDisplay,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Segmented control bar
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ModernCard,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val timerOptions = listOf(
                                    30 to "30s",
                                    60 to "1m",
                                    90 to "1.5m",
                                    120 to "2m",
                                    180 to "3m",
                                    300 to "5m"
                                )
                                timerOptions.forEach { (sec, label) ->
                                    val isSelected = uiState.settings.timerDurationSeconds == sec
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) ModernIndigo else Color.Transparent,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { onTimerDurationChange(sec) }
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                                color = if (isSelected) Color.White else TextMuted
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }

        // FIXED BOTTOM CTA
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = ModernSurface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onStartGame,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ModernIndigo),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("start_game_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isNepali) "खेल सुरु गर्नुहोस्" else "START MATCH",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArenaHeroCard(
    title: String,
    subtitle: String,
    xpBadge: String,
    difficultyBadge: String,
    difficultyColor: Color,
    drawableRes: Int,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ModernSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Artwork Illustration
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Flat Dark Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.25f),
                                Color(0xFF0F172A).copy(alpha = 0.75f),
                                Color(0xFF020617).copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            // Content inside Card
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: XP badge & Difficulty tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = xpBadge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = ModernEmerald,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = difficultyColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = difficultyBadge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = difficultyColor,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Bottom Row: Title + Subtitle and Checked Circle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) ModernIndigo else Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.size(28.dp)
                    ) {
                        if (isSelected) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
