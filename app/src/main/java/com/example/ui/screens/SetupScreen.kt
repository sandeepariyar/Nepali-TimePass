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

    Scaffold(containerColor = HomeBg, bottomBar = {
        Surface(color = HomeBg, shadowElevation = 12.dp) {
            Column(Modifier.navigationBarsPadding().imePadding().padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 12.dp)) {
                Button(
                    onClick = onStartGame, enabled = uiState.totalMoviesCount > 0, interactionSource = startInteraction,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 66.dp).graphicsLayer { scaleX = startScale; scaleY = startScale }.testTag("start_game_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Lime, contentColor = HomeBg)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(if (ne) "खेल सुरु • ${uiState.settings.timerDurationSeconds}s" else
                        "Start game • ${uiState.settings.timerDurationSeconds}s", fontSize = 19.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }) { insets ->
        Column(Modifier.padding(insets).fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 14.dp)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showSettings = true }) { Icon(Icons.Default.Settings, contentDescription = if (ne) "सेटिङ" else "Settings", tint = Color.White) }
                Surface(shape = RoundedCornerShape(50), color = PanelBg, border = BorderStroke(1.dp, CardStroke)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        listOf(AppLanguage.NEPALI, AppLanguage.ENGLISH).forEach { language ->
                            val active = uiState.appLanguage == language
                            Box(Modifier.heightIn(min = 48.dp).clip(RoundedCornerShape(50)).selectable(
                                selected = active, role = Role.Tab, onClick = { if (!active) onToggleAppLanguage() }
                            ).testTag("setup_language_${language.name}").background(if (active) Color(0xFF263526) else Color.Transparent).padding(horizontal = 15.dp, vertical = 9.dp)) {
                                Text(if (language == AppLanguage.NEPALI) "नेपाली" else "EN",
                                    color = if (active) Lime else Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                IconButton(onClick = { showRules = true }) { Icon(Icons.Default.HelpOutline, contentDescription = if (ne) "नियम" else "How to play", tint = Color.White) }
            }

            Image(
                painter = painterResource(R.drawable.home_header),
                contentDescription = if (ne) "नेपाली टाइमपास" else "Nepali TimePass",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().heightIn(max = 220.dp).padding(horizontal = 12.dp).clip(RoundedCornerShape(20.dp))
            )
            Spacer(Modifier.height(16.dp))

            HomePanel {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        Text(if (ne) "तपाईंका ग्याङ" else "Your gangs", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(if (ne) "नाम र टोली छान्नुहोस्" else "Name and customize your teams", color = Muted, fontSize = 12.sp)
                    }
                    OutlinedButton(onClick = onRandomizeGangs,
                        modifier = Modifier.testTag("randomize_gangs_button"),
                        border = BorderStroke(1.dp, Lime)) {
                        Icon(Icons.Default.Shuffle, contentDescription = null, tint = Lime, modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(5.dp))
                        Text(if (ne) "दुवै फेरौँ" else "Shuffle both", color = Lime, fontSize = 11.sp)
                    }
                }
                Spacer(Modifier.height(14.dp))
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    if (maxWidth >= 280.dp && LocalDensity.current.fontScale <= 1.2f) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            HomeGangCard(uiState.team1, ne, R.drawable.gang_lightning,
                                onTeam1NameChange, onTeam1ColorChange, onRandomizeTeam1, Modifier.weight(1f))
                            HomeGangCard(uiState.team2, ne, R.drawable.gang_flame,
                                onTeam2NameChange, onTeam2ColorChange, onRandomizeTeam2, Modifier.weight(1f))
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            HomeGangCard(uiState.team1, ne, R.drawable.gang_lightning,
                                onTeam1NameChange, onTeam1ColorChange, onRandomizeTeam1, Modifier.fillMaxWidth())
                            HomeGangCard(uiState.team2, ne, R.drawable.gang_flame,
                                onTeam2NameChange, onTeam2ColorChange, onRandomizeTeam2, Modifier.fillMaxWidth())
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            HomePanel(Modifier.testTag("cinema_section")) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VideoLibrary, null, tint = Color(0xFFFFB64F))
                    Spacer(Modifier.width(9.dp))
                    Column(Modifier.weight(1f)) {
                        Text(if (ne) "चलचित्र छान्नुहोस्" else "Pick your cinema", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(if (ne) "एक वा धेरै छान्नुहोस्" else "Choose one or more categories", fontSize = 12.sp, color = Muted)
                    }
                    TextButton(onClick = { onSetLanguagePreset(SupportedCategories.toSet()) }) {
                        Text(if (ne) "सबै" else "All", color = Lime)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SupportedCategories.forEach { category ->
                        val drawable = when (category) {
                            MovieLanguage.NEPALI -> R.drawable.cat_nepali
                            MovieLanguage.HINDI_MODERN -> R.drawable.cat_bollywood
                            MovieLanguage.HINDI_CLASSIC -> R.drawable.cat_classics
                            MovieLanguage.SOUTH_INDIAN -> R.drawable.cat_south
                            else -> R.drawable.cat_nepali
                        }
                        val english = when (category) {
                            MovieLanguage.NEPALI -> "Nepali"
                            MovieLanguage.HINDI_MODERN -> "Bollywood"
                            MovieLanguage.HINDI_CLASSIC -> "Classics"
                            MovieLanguage.SOUTH_INDIAN -> "South"
                            else -> category.displayName
                        }
                        val nepali = when (category) {
                            MovieLanguage.NEPALI -> "नेपाली"
                            MovieLanguage.HINDI_MODERN -> "हिन्दी"
                            MovieLanguage.HINDI_CLASSIC -> "पुरानो"
                            MovieLanguage.SOUTH_INDIAN -> "दक्षिण"
                            else -> category.displayName
                        }
                        val active = category in uiState.settings.selectedLanguages
                        val selectionColor by animateColorAsState(if (active) Lime else CardStroke, label = "cinema_selection")
                        Column(Modifier.width(112.dp).clip(RoundedCornerShape(17.dp))
                            .border(2.dp, selectionColor, RoundedCornerShape(17.dp))
                            .toggleable(value = active, role = Role.Checkbox, onValueChange = { onToggleLanguage(category) })
                            .testTag("category_${category.name}")) {
                            Box {
                                Image(painterResource(drawable), null, contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxWidth().height(130.dp))
                                if (active) Icon(Icons.Default.CheckCircle, null, tint = Lime,
                                    modifier = Modifier.align(Alignment.TopEnd).padding(7.dp))
                            }
                            Text(if (ne) nepali else english, Modifier.fillMaxWidth().background(PanelBg).padding(vertical = 8.dp),
                                color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                maxLines = 1, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (ne) "${uiState.totalMoviesCount} चलचित्र तयार" else "${uiState.totalMoviesCount} films ready", color = Muted, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    TextButton(onClick = { showLibrary = true }, modifier = Modifier.testTag("film_library_button")) { Text(if (ne) "सङ्ग्रह →" else "Film library →", color = Lime, fontSize = 12.sp) }
                }
            }
            Spacer(Modifier.height(12.dp))
            HomePanel(Modifier.testTag("duration_section")) {
                Text(if (ne) "पालोको समय" else "Round duration", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).selectableGroup(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    Durations.forEach { duration ->
                        val active = uiState.settings.timerDurationSeconds == duration
                        Surface(shape = RoundedCornerShape(50),
                            color = if (active) Lime else Color(0xFF252D41),
                            border = BorderStroke(1.dp, if (active) Lime else CardStroke),
                            modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp).selectable(selected = active, role = Role.RadioButton, onClick = { onTimerDurationChange(duration) }).testTag("duration_$duration")) {
                            Text("${duration}s", Modifier.padding(horizontal = 12.dp, vertical = 11.dp),
                                color = if (active) HomeBg else Color.White,
                                fontWeight = if (active) FontWeight.Black else FontWeight.Medium, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomePanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier.fillMaxWidth().padding(horizontal = 12.dp).clip(RoundedCornerShape(23.dp))
        .background(PanelBg).border(1.dp, CardStroke, RoundedCornerShape(23.dp)).padding(14.dp), content = content)
}

@Composable
private fun HomeGangCard(
    team: Team, ne: Boolean, drawable: Int,
    onName: (String) -> Unit, onColor: (Long) -> Unit, onShuffle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = Color(team.colorHex)
    Column(modifier.clip(RoundedCornerShape(18.dp)).background(tint.copy(alpha = 0.10f))
        .border(1.dp, tint.copy(alpha = 0.55f), RoundedCornerShape(18.dp)).padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedContent(targetState = team.name,
            transitionSpec = { (fadeIn() + scaleIn(initialScale = .88f)) togetherWith (fadeOut() + scaleOut(targetScale = .92f)) },
            label = "gang_icon_shuffle") {
            Image(painterResource(drawable), contentDescription = it,
                modifier = Modifier.size(72.dp), contentScale = ContentScale.Fit)
        }
        Text(if (ne) "ग्याङ ${team.id}" else "Gang ${team.id}", color = Muted, fontSize = 11.sp)
        OutlinedTextField(value = team.name, onValueChange = { onName(it.take(48)) },
            modifier = Modifier.fillMaxWidth().testTag("team_${team.id}_name"),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White),
            singleLine = false, maxLines = 2, minLines = 2, trailingIcon = { Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp)) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = tint, unfocusedBorderColor = Color.Transparent))
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            GangColorPalette.COLORS.forEach { choice ->
                val active = team.colorHex == choice.colorHex
                Box(Modifier.size(48.dp).clip(CircleShape)
                    .semantics { contentDescription = if (ne) choice.nameNepali else choice.name }
                    .selectable(selected = active, role = Role.RadioButton, onClick = { onColor(choice.colorHex) }),
                    contentAlignment = Alignment.Center) {
                    Box(Modifier.size(25.dp).clip(CircleShape).background(Color(choice.colorHex))
                        .border(if (active) 3.dp else 1.dp, if (active) Color.White else CardStroke, CircleShape))
                }
            }
        }
        Spacer(Modifier.height(5.dp))
        TextButton(onClick = onShuffle, contentPadding = PaddingValues(horizontal = 3.dp)) {
            Icon(Icons.Default.Casino, null, tint = tint, modifier = Modifier.size(17.dp))
            Spacer(Modifier.width(4.dp))
            Text(if (ne) "नयाँ नाम" else "Shuffle", color = tint, fontSize = 12.sp)
        }
    }
}
