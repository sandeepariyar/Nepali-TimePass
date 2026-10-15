package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.model.AppLanguage
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState
import java.util.Locale

@Composable
fun ActingScreen(uiState: GameUiState, onToggleReveal: () -> Unit, onTogglePauseTimer: () -> Unit,
    onGuessCorrect: () -> Unit, onGuessWrongOrPass: () -> Unit, onSkipMovie: () -> Unit,
    onConcludeGame: () -> Unit, onToggleSound: () -> Unit = {}) {
    val ne = uiState.appLanguage == AppLanguage.NEPALI
    var confirmEnd by rememberSaveable { mutableStateOf(false) }
    if (confirmEnd) AlertDialog(onDismissRequest = { confirmEnd = false },
        title = { Text(if (ne) "खेल समाप्त गर्ने?" else "Finish this match?") },
        text = { Text(if (ne) "समय चलिरहन्छ। समाप्त गरेपछि विजेता देखिनेछ।" else "The timer keeps running. Finishing takes you to the winners.") },
        confirmButton = { TextButton(onClick = { confirmEnd = false; onConcludeGame() }) { Text(if (ne) "समाप्त" else "Finish") } },
        dismissButton = { TextButton(onClick = { confirmEnd = false }) { Text(if (ne) "खेल जारी" else "Keep playing") } })
    CinemaBackdrop {
        BoxWithConstraints(Modifier.fillMaxSize().padding(16.dp)) {
            val wide = maxWidth > maxHeight
            val showPoster = maxHeight > 720.dp
            val needsScroll = !wide && (maxHeight < 480.dp || LocalDensity.current.fontScale > 1.5f)
            val bodyModifier = if (needsScroll) Modifier.fillMaxSize().verticalScroll(rememberScrollState()) else Modifier.fillMaxSize()
            Column(bodyModifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(if (ne) "राउन्ड ${uiState.currentRoundNumber} • अभिनय" else "ROUND ${uiState.currentRoundNumber} • ON STAGE", color = ModernAmber, style = MaterialTheme.typography.labelLarge)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("${uiState.team1.name}  ${uiState.team1.score}", Modifier.weight(1f), color = TextPrimary, maxLines = 2)
                    Text("${uiState.team2.score}  ${uiState.team2.name}", Modifier.weight(1f), textAlign = TextAlign.End, color = TextPrimary, maxLines = 2)
                }
                if (wide) Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                        Countdown(uiState, ne)
                        ActingControls(uiState, ne, onGuessCorrect, onGuessWrongOrPass, onSkipMovie, onTogglePauseTimer)
                    }
                    Box(Modifier.weight(1f).verticalScroll(rememberScrollState())) { SecretFilm(uiState, ne, onToggleReveal, false) }
                } else {
                    Countdown(uiState, ne)
                    Box(if (needsScroll) Modifier.fillMaxWidth().heightIn(min = 120.dp) else Modifier.weight(1f).verticalScroll(rememberScrollState())) { SecretFilm(uiState, ne, onToggleReveal, showPoster) }
                    ActingControls(uiState, ne, onGuessCorrect, onGuessWrongOrPass, onSkipMovie, onTogglePauseTimer)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = onToggleSound) { Text(if (uiState.settings.soundEffectsEnabled) { if (ne) "आवाज बन्द" else "Mute" } else { if (ne) "आवाज खोल्नुहोस्" else "Unmute" }, color = ModernAmber) }
                    TextButton(onClick = { confirmEnd = true }) { Text(if (ne) "खेल समाप्त" else "Finish match", color = TextSecondary) }
                }
                if (uiState.backgroundTimerUnavailable) Text(if (ne) "पृष्ठभूमि टाइमर उपलब्ध छैन। एप खुला राख्नुहोस्।" else "Background timer unavailable. Keep the app open.", color = ModernAmber, style = MaterialTheme.typography.labelSmall)
                else if (!androidx.core.app.NotificationManagerCompat.from(LocalContext.current).areNotificationsEnabled())
                    Text(if (ne) "सूचना बन्द छ। लक स्क्रिनमा समय देखिँदैन।" else "Notifications off • Lock-screen countdown hidden", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
            }
        }
        StageLighting(uiState.remainingSeconds, uiState.settings.timerDurationSeconds, uiState.isTimerRunning, Modifier.matchParentSize())
    }
}

@Composable
private fun Countdown(state: GameUiState, ne: Boolean) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(String.format(Locale.ROOT, "%02d:%02d", state.remainingSeconds / 60, state.remainingSeconds % 60),
            fontSize = 44.sp, lineHeight = 52.sp, fontWeight = FontWeight.Black,
            color = TextPrimary, modifier = Modifier.testTag("countdown"))
        Text(if (state.isTimerPaused) { if (ne) "मध्यान्तर • समय रोकिएको छ" else "INTERMISSION • PAUSED" }
            else if (state.remainingSeconds <= 10) { if (ne) "अन्तिम मौका!" else "THE GRAND FINALE!" }
            else { if (ne) "नबोली अभिनय गर्नुहोस्" else "LET YOUR ACTING DO THE TALKING" },
            color = ModernAmber, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
    }
}

@Composable
private fun SecretFilm(state: GameUiState, ne: Boolean, onReveal: () -> Unit, showPoster: Boolean) {
    CinemaPanel {
        AnimatedContent(targetState = state.currentMovie, label = "movie_skip") { movie ->
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (state.isCardRevealed && movie != null) {
                    if (showPoster) MoviePosterBadge(movie, width = 74.dp, height = 100.dp)
                    Text(if (ne && movie.nativeTitle.isNotBlank()) movie.nativeTitle else movie.title,
                        style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center, modifier = Modifier.testTag("movie_title"))
                    if (ne && movie.nativeTitle.isNotBlank()) Text(movie.title, color = TextSecondary, textAlign = TextAlign.Center)
                    Text("${movie.year} • ${movie.genre}", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                    if (movie.actors.isNotBlank()) Text(movie.actors, color = TextSecondary, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
                    if (movie.hint.isNotBlank()) Text(movie.hint, color = TextSecondary, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
                } else {
                    Text(if (ne) "गोप्य चलचित्र" else "FOR THE ACTOR’S EYES", color = ModernAmber)
                    Text(if (ne) "आफ्नो फिल्म हेर्नुहोस्।" else "Ready for your close-up?", color = TextPrimary, textAlign = TextAlign.Center)
                }
                CinemaButton(if (state.isCardRevealed) { if (ne) "फिल्म लुकाउनुहोस्" else "Hide film" } else { if (ne) "फिल्म हेर्नुहोस्" else "Reveal film" }, onReveal,
                    Modifier.fillMaxWidth().testTag("reveal_movie_button"), secondary = true)
            }
        }
    }
}

@Composable
private fun ActingControls(state: GameUiState, ne: Boolean, correct: () -> Unit, pass: () -> Unit, skip: () -> Unit, pause: () -> Unit) {
    val canGuess = state.isTimerRunning && state.remainingSeconds > 0
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        CinemaButton(if (ne) "मिल्यो! +१" else "CORRECT! +1", correct, Modifier.fillMaxWidth().testTag("guess_correct_button"), enabled = canGuess)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CinemaButton(if (ne) "पास" else "Pass", pass, Modifier.weight(1f).testTag("guess_wrong_button"), secondary = true, enabled = canGuess)
            CinemaButton(if (ne) "फिल्म फेरौँ" else "Skip film", skip, Modifier.weight(1f).testTag("skip_movie_button"), secondary = true, enabled = canGuess)
            CinemaButton(if (state.isTimerPaused) { if (ne) "जारी" else "Resume" } else { if (ne) "रोकौँ" else "Pause" }, pause, Modifier.weight(1f).testTag("pause_timer_button"), secondary = true)
        }
    }
}
