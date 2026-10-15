package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
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
    val ne = uiState.appLanguage == AppLanguage.NEPALI
    var rules by rememberSaveable { mutableStateOf(false) }
    var settings by rememberSaveable { mutableStateOf(false) }
    var custom by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }
    if (rules) RulesDialog(onDismiss = { rules = false }, isNepali = ne)
    if (custom) AddCustomMovieDialog(onDismiss = { custom = false }, onAddCustomMovie = onAddCustomMovie, isNepali = ne)
    if (settings) AlertDialog(onDismissRequest = { settings = false },
        title = { Text(if (ne) "खेल सेटिङ" else "Game settings") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(if (ne) "पालोको समय" else "Round duration")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(30, 60, 90, 120, 180, 300, 600).forEach { duration ->
                    FilterChip(selected = uiState.settings.timerDurationSeconds == duration,
                        onClick = { onTimerDurationChange(duration) }, label = { Text("${duration}s") })
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (ne) "खेलको आवाज" else "Game sound", Modifier.weight(1f))
                Switch(uiState.settings.soundEffectsEnabled, { onToggleSound() })
            }
            Text(if (ne) "फोनको आवाज र सूचना सेटिङ लागू हुन्छन्। लक स्क्रिनमा समय देखाउन सूचना अनुमति दिनुहोस्।" else
                "Device sound and notification settings apply. Allow notifications to see the countdown on your lock screen.")
        } }, confirmButton = { TextButton(onClick = { settings = false }) { Text(if (ne) "ठीक छ" else "Done") } })
    CinemaScene {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = { settings = true }) { Text(if (ne) "सेटिङ" else "Settings", color = TextPrimary) }
            TextButton(onClick = onToggleAppLanguage) { Text(if (ne) "ENGLISH" else "नेपाली", color = ModernAmber) }
            TextButton(onClick = { rules = true }) { Text(if (ne) "नियम" else "How to play", color = TextPrimary) }
        }
        CinemaHeading(if (ne) "रमाइलो सुरु गरौँ" else "PARTY MODE", if (ne) "नेपाली टाइमपास" else "Nepali TimePass",
            if (ne) "आफ्नो ग्याङ छान्नुहोस्। मञ्चमा छाउनुहोस्।" else "Your friends. Your films. Your game.")
        CinemaButton(if (ne) "खेल सुरु गरौँ • ${uiState.settings.timerDurationSeconds}s" else "Start game • ${uiState.settings.timerDurationSeconds}s",
            onStartGame, Modifier.fillMaxWidth().testTag("start_game_button"), enabled = uiState.totalMoviesCount > 0)
        CinemaPanel {
            Text(if (ne) "तपाईंका ग्याङ" else "Your gangs", color = ModernAmber)
            GangEditor(uiState.team1, ne, onTeam1NameChange, onTeam1ColorChange, onRandomizeTeam1)
            HorizontalDivider(color = ModernCardAlt)
            GangEditor(uiState.team2, ne, onTeam2NameChange, onTeam2ColorChange, onRandomizeTeam2)
            TextButton(onClick = onRandomizeGangs, modifier = Modifier.fillMaxWidth().testTag("randomize_gangs_button")) {
                Text(if (ne) "दुवै ग्याङ फेरौँ ↻" else "Surprise both gangs ↻", color = ModernAmber)
            }
        }
        CinemaPanel {
            Text(if (ne) "चलचित्र छान्नुहोस्" else "Pick your cinema", color = ModernAmber)
            MovieLanguage.entries.filter { it != MovieLanguage.ALL }.forEach { category ->
                val label = if (ne) when(category) {
                    MovieLanguage.NEPALI -> "नेपाली चलचित्र"
                    MovieLanguage.HINDI_MODERN -> "आधुनिक बलिउड"
                    MovieLanguage.HINDI_CLASSIC -> "पुरानो बलिउड"
                    MovieLanguage.SOUTH_INDIAN -> "दक्षिण भारतीय चलचित्र"
                    else -> "सबै चलचित्र"
                } else category.displayName
                val chosen = category in uiState.settings.selectedLanguages
                val chipScale by animateFloatAsState(if (chosen) 1f else .985f,
                    spring(stiffness = 600f), label = "category_selection")
                FilterChip(selected = chosen, border = null, shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    onClick = { onToggleLanguage(category) }, modifier = Modifier.fillMaxWidth().graphicsLayer { scaleX = chipScale; scaleY = chipScale }.testTag("category_${category.name}"),
                    label = { Text(label, modifier = Modifier.padding(vertical = 8.dp)) })
            }
            TextButton(onClick = { onSetLanguagePreset(MovieLanguage.entries.filter { it != MovieLanguage.ALL }.toSet()) }) { Text(if (ne) "सबै मिसाउनुहोस्" else "Mix every cinema") }
            Text(if (ne) "${uiState.totalMoviesCount} चलचित्र तयार" else "${uiState.totalMoviesCount} films ready", color = TextSecondary)
            Text(if (uiState.isOnlineSyncing) { if (ne) "नयाँ चलचित्र थपिँदैछन्…" else "Bringing in more films…" }
                else { if (ne) "अनलाइन चलचित्र र पोस्टर उपलब्ध छन्" else "Online films and posters available" }, color = TextSecondary)
            CinemaButton(if (ne) "चलचित्र अपडेट" else "Refresh film library", onFetchMoreOnline, Modifier.fillMaxWidth(), secondary = true, enabled = !uiState.isOnlineSyncing)
            OutlinedTextField(query, { query = it }, label = { Text(if (ne) "अनलाइन फिल्म खोज्नुहोस्" else "Search online films") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            CinemaButton(if (ne) "खोजेर थप्नुहोस्" else "Search & add", { onSearchOnlineAndAdd(query.trim()); query = "" }, Modifier.fillMaxWidth(), secondary = true, enabled = query.isNotBlank() && !uiState.isOnlineSyncing)
            TextButton(onClick = { custom = true }, modifier = Modifier.fillMaxWidth().testTag("add_custom_movie_button")) { Text(if (ne) "+ आफ्नै चलचित्र थप्नुहोस्" else "+ Add your own film", color = ModernAmber) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GangEditor(team: Team, ne: Boolean, onName: (String) -> Unit, onColor: (Long) -> Unit, onRandom: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AnimatedContent(targetState = team.name,
                transitionSpec = { (fadeIn() + scaleIn(initialScale = .82f)) togetherWith (fadeOut() + scaleOut(targetScale = .92f)) },
                label = "gang_shuffle") { shuffledName ->
                TeamEmblem(team.id, Color(team.colorHex), modifier = Modifier.semantics { contentDescription = shuffledName }, size = 64.dp)
            }
            TextField(team.name, { onName(it.take(48)) }, label = { Text(if (ne) "ग्याङ ${team.id}" else "Gang ${team.id}") },
                modifier = Modifier.weight(1f).testTag("team_${team.id}_name"), maxLines = 2, shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp), colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
        }
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            GangColorPalette.COLORS.forEach { color ->
                IconButton(onClick = { onColor(color.colorHex) }, modifier = Modifier.size(48.dp).semanticsColor(if (ne) color.nameNepali else color.name, team.colorHex == color.colorHex)) {
                    Surface(Modifier.size(22.dp), shape = CircleShape, color = Color(color.colorHex),
                        border = BorderStroke(if (team.colorHex == color.colorHex) 3.dp else 0.dp, TextPrimary)) {
                        androidx.compose.foundation.layout.Box(Modifier)
                    }
                }
            }
        }
        TextButton(onClick = onRandom, modifier = Modifier.fillMaxWidth()) { Text(if (ne) "नयाँ नाम ↻" else "Shuffle this gang ↻", color = ModernAmber) }
    }
}

private fun Modifier.semanticsColor(label: String, chosen: Boolean): Modifier = this.then(
    Modifier.semantics { contentDescription = label; selected = chosen }
)
