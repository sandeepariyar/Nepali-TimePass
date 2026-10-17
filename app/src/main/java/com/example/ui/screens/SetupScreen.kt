package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.*
import com.example.viewmodel.GameUiState

private val HomeBg = Color(0xFF090C15)
private val PanelBg = Color(0xFF161B29)
private val Lime = Color(0xFFB8F76D)
private val Muted = Color(0xFFACB3C6)
private val CardStroke = Color(0xFF2C354A)
private val Durations = listOf(30, 60, 90, 120, 180, 300, 600)
private val SupportedCategories = listOf(
    MovieLanguage.NEPALI, MovieLanguage.HINDI_MODERN,
    MovieLanguage.HINDI_CLASSIC, MovieLanguage.SOUTH_INDIAN
)

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
    val ne = uiState.appLanguage == AppLanguage.NEPALI
    var showRules by rememberSaveable { mutableStateOf(false) }
    var showSettings by rememberSaveable { mutableStateOf(false) }
    var showLibrary by rememberSaveable { mutableStateOf(false) }
    var showCustom by rememberSaveable { mutableStateOf(false) }
    var editingTeam by rememberSaveable { mutableStateOf<Int?>(null) }
    var search by rememberSaveable { mutableStateOf("") }
    val startInteraction = remember { MutableInteractionSource() }
    val startPressed by startInteraction.collectIsPressedAsState()
    val startScale by animateFloatAsState(if (startPressed) .97f else 1f,
        spring(dampingRatio = .8f, stiffness = 650f), label = "home_start_press")

    if (showRules) RulesDialog(onDismiss = { showRules = false }, isNepali = ne)
    if (showCustom) AddCustomMovieDialog(onDismiss = { showCustom = false }, onAddCustomMovie = onAddCustomMovie, isNepali = ne)

    if (showSettings) AlertDialog(
        onDismissRequest = { showSettings = false },
        title = { Text(if (ne) "सेटिङ" else "Settings") },
        text = {
            Column(Modifier.heightIn(max = 400.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if (ne) "खेलको आवाज" else "Game sound")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (ne) "आवाज चालू" else "Sound effects", Modifier.weight(1f))
                    Switch(checked = uiState.settings.soundEffectsEnabled, onCheckedChange = { onToggleSound() })
                }
                TextButton(onClick = { onSetLanguagePreset(SupportedCategories.toSet()); showSettings = false }) {
                    Text(if (ne) "सबै चलचित्र मिसाउनुहोस्" else "Mix every cinema")
                }
                Text(if (ne) "लक स्क्रिनमा समय देखाउन सूचना अनुमति दिनुहोस्।" else
                    "Allow notifications to show the countdown on your lock screen.", color = Muted)
            }
        },
        confirmButton = { TextButton(onClick = { showSettings = false }) { Text(if (ne) "ठीक छ" else "Done") } }
    )

    if (showLibrary) HomeFilmLibraryDialog(
        ne = ne, movieCount = uiState.totalMoviesCount, syncing = uiState.isOnlineSyncing,
        search = search, onSearchChange = { search = it },
        onRefresh = onFetchMoreOnline,
        onSearch = { onSearchOnlineAndAdd(search.trim()); search = "" },
        onCustom = { showLibrary = false; showCustom = true }, onDismiss = { showLibrary = false }
    )

    editingTeam?.let { id ->
        HomeGangEditDialog(
            team = if (id == 1) uiState.team1 else uiState.team2, ne = ne,
            onName = if (id == 1) onTeam1NameChange else onTeam2NameChange,
            onColor = if (id == 1) onTeam1ColorChange else onTeam2ColorChange,
            onShuffle = if (id == 1) onRandomizeTeam1 else onRandomizeTeam2,
            onDismiss = { editingTeam = null }
        )
    }
    Scaffold(containerColor = HomeBg, contentWindowInsets = WindowInsets(0, 0, 0, 0), bottomBar = {
        Surface(color = HomeBg) {
            Button(onClick = onStartGame, enabled = uiState.totalMoviesCount > 0, interactionSource = startInteraction,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp).fillMaxWidth()
                    .heightIn(min = 56.dp).graphicsLayer { scaleX = startScale; scaleY = startScale }.testTag("start_game_button"),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Lime, contentColor = HomeBg)) {
                Icon(Icons.Default.PlayArrow, null, Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (ne) "खेल सुरु • ${uiState.settings.timerDurationSeconds}s" else "Start game • ${uiState.settings.timerDurationSeconds}s",
                    fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }) { insets ->
        BoxWithConstraints(Modifier.padding(insets).fillMaxSize()) {
            val compact = maxHeight >= 440.dp && LocalDensity.current.fontScale <= 1.25f
            val bannerHeight = if (compact) (maxHeight * .22f).coerceIn(72.dp, 130.dp) else 84.dp
            val body = if (compact) Modifier.fillMaxSize() else Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            var durationMenu by remember { mutableStateOf(false) }
            Column(body.padding(horizontal = 12.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = { showSettings = true }) { Icon(Icons.Default.Settings, if (ne) "सेटिङ" else "Settings", tint = Color.White) }
                    Row(Modifier.selectableGroup()) {
                        AppLanguage.entries.forEach { language ->
                            val active = uiState.appLanguage == language
                            Box(Modifier.clip(RoundedCornerShape(16.dp)).selectable(selected = active, role = Role.Tab,
                                onClick = { if (!active) onToggleAppLanguage() })
                                .testTag("setup_language_${language.name}").background(if (active) Color(0xFF263526) else Color.Transparent)
                                .sizeIn(minWidth = 48.dp, minHeight = 48.dp).padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                                Text(if (language == AppLanguage.NEPALI) "नेपाली" else "EN", color = if (active) Lime else Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    IconButton(onClick = onRandomizeGangs, modifier = Modifier.testTag("randomize_gangs_button")) {
                        Icon(Icons.Default.Shuffle, if (ne) "दुवै ग्याङ फेरौँ" else "Shuffle both gangs", tint = Lime)
                    }
                    IconButton(onClick = { showRules = true }) { Icon(Icons.Default.HelpOutline, if (ne) "नियम" else "How to play", tint = Color.White) }
                }
                Image(painterResource(R.drawable.home_header), if (ne) "नेपाली टाइमपास" else "Nepali TimePass",
                    Modifier.fillMaxWidth().height(bannerHeight).clip(RoundedCornerShape(18.dp)), contentScale = ContentScale.Fit)
                HomePanel {
                    Text(if (ne) "तपाईंका ग्याङ • नाम र रङ बदल्न थिच्नुहोस्" else "Your gangs • Tap to edit name & color", color = Muted, fontSize = 12.sp)
                    Spacer(Modifier.height(4.dp))
                    if (LocalDensity.current.fontScale > 1.3f) {
                        CompactGangCard(uiState.team1, ne, R.drawable.gang_lightning, { editingTeam = 1 }, Modifier.fillMaxWidth())
                        Spacer(Modifier.height(6.dp))
                        CompactGangCard(uiState.team2, ne, R.drawable.gang_flame, { editingTeam = 2 }, Modifier.fillMaxWidth())
                    } else Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CompactGangCard(uiState.team1, ne, R.drawable.gang_lightning, { editingTeam = 1 }, Modifier.weight(1f))
                        CompactGangCard(uiState.team2, ne, R.drawable.gang_flame, { editingTeam = 2 }, Modifier.weight(1f))
                    }
                }
                HomePanel(Modifier.testTag("cinema_section")) {
                    Text(if (ne) "चलचित्र छान्नुहोस्" else "Pick your cinema", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        SupportedCategories.forEach { category ->
                            val image = when (category) {
                                MovieLanguage.NEPALI -> R.drawable.cat_nepali
                                MovieLanguage.HINDI_MODERN -> R.drawable.cat_bollywood
                                MovieLanguage.HINDI_CLASSIC -> R.drawable.cat_classics
                                else -> R.drawable.cat_south
                            }
                            val label = when (category) {
                                MovieLanguage.NEPALI -> if (ne) "नेपाली" else "Nepali"
                                MovieLanguage.HINDI_MODERN -> if (ne) "बलिउड" else "Bollywood"
                                MovieLanguage.HINDI_CLASSIC -> if (ne) "पुरानो" else "Classics"
                                else -> if (ne) "दक्षिण" else "South"
                            }
                            val active = category in uiState.settings.selectedLanguages
                            val edge by animateColorAsState(if (active) Lime else CardStroke, label = "category_highlight")
                            Column(Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).border(2.dp, edge, RoundedCornerShape(12.dp))
                                .toggleable(active, role = Role.Checkbox, onValueChange = { onToggleLanguage(category) }).testTag("category_${category.name}"),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Box {
                                    Image(painterResource(image), null, Modifier.fillMaxWidth().height(48.dp), contentScale = ContentScale.Crop)
                                    if (active) Icon(Icons.Default.CheckCircle, null, Modifier.align(Alignment.TopEnd).padding(3.dp).size(16.dp), tint = Lime)
                                }
                                Text(label, Modifier.padding(vertical = 4.dp), color = Color.White, fontSize = 11.sp, maxLines = 1)
                            }
                        }
                    }
                }
                HomePanel(Modifier.testTag("duration_section")) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (ne) "पालोको समय" else "Round time", color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        Box {
                            TextButton(onClick = { durationMenu = true }, modifier = Modifier.testTag("duration_menu")) {
                                Text("${uiState.settings.timerDurationSeconds}s ▾", color = Lime, fontWeight = FontWeight.Bold)
                            }
                            DropdownMenu(durationMenu, onDismissRequest = { durationMenu = false }) {
                                Durations.forEach { duration ->
                                    DropdownMenuItem(text = { Text("${duration}s") }, onClick = { onTimerDurationChange(duration); durationMenu = false },
                                        modifier = Modifier.testTag("duration_$duration"))
                                }
                            }
                        }
                        IconButton(onClick = { showLibrary = true }, modifier = Modifier.testTag("film_library_button")) {
                            Icon(Icons.Default.VideoLibrary, if (ne) "चलचित्र सङ्ग्रह" else "Film library", tint = Lime)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomePanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(PanelBg).padding(8.dp), content = content)
}

@Composable
private fun CompactGangCard(team: Team, ne: Boolean, drawable: Int, onEdit: () -> Unit, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(14.dp)).background(Color(team.colorHex).copy(alpha = .12f))
        .border(1.dp, Color(team.colorHex).copy(alpha = .6f), RoundedCornerShape(14.dp)).clickable(onClick = onEdit)
        .testTag("team_${team.id}_card").padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedContent(targetState = team.emoji to drawable, label = "gang_shuffle") { emblem ->
            Image(painterResource(emblem.second), null, Modifier.size(40.dp), contentScale = ContentScale.Fit)
        }
        Text(team.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp,
            maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(if (ne) "बदल्नुहोस्" else "Edit", color = Muted, fontSize = 10.sp)
    }
}
