package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.R
import com.example.model.AppLanguage
import com.example.model.MovieLanguage
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun SplashScreen(appLanguage: AppLanguage = AppLanguage.NEPALI, onToggleLanguage: () -> Unit = {},
    selectedLanguages: Set<MovieLanguage> = emptySet(),
    onSelectCinema: (Set<MovieLanguage>) -> Unit = {}, onContinue: () -> Unit) {
    val ne = appLanguage == AppLanguage.NEPALI
    var showRules by rememberSaveable { mutableStateOf(false) }
    if (showRules) RulesDialog(onDismiss = { showRules = false }, isNepali = ne)
    var entered by remember { mutableStateOf(false) }
    val reveal by animateFloatAsState(if (entered) 1f else 0f, tween(360), label = "home_entrance")
    LaunchedEffect(Unit) { entered = true }
    CinemaBackdrop {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val heroHeight = (maxHeight - 340.dp).coerceIn(320.dp, 560.dp)
            Column(Modifier.widthIn(max = 600.dp).fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("TIMEPASS", color = TextPrimary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = onToggleLanguage, modifier = Modifier.testTag("welcome_language_button")) {
                        Text(if (ne) "English / नेपाली" else "नेपाली / English", color = ModernAmber)
                    }
                }
                Column(Modifier.fillMaxWidth().height(heroHeight).graphicsLayer { alpha = reveal }) {
                Column(Modifier.graphicsLayer { alpha = reveal; translationY = (1f - reveal) * 16.dp.toPx() }) {
                    Text(if (ne) "तपाईंको ग्याङ।\nतपाईंको फिल्म।" else "Your gang.\nYour movie.",
                        color = TextPrimary, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                    Text(if (ne) "तपाईंको पल।" else "Your moment.", color = ModernAmber,
                        style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                    Text(if (ne) "अभिनय गरौँ, रमाइलो गरौँ" else "Act it out. Make memories.", color = ModernIndigoLight, style = MaterialTheme.typography.bodyMedium)
                }
                    Box(Modifier.fillMaxWidth().weight(1f)) {
                    Image(painterResource(R.drawable.home_hero), contentDescription = null,
                        contentScale = ContentScale.Crop, alignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize())
                    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(
                        listOf(ModernBg, Color.Transparent, Color.Transparent, ModernBg))))
                    }
                }
                Text(if (ne) "चलचित्र छान्नुहोस्" else "Pick your cinema",
                    color = TextPrimary, fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val choices = listOf(
                        (if (ne) "नेपाली" else "Nepali") to setOf(MovieLanguage.NEPALI),
                        (if (ne) "बलिउड" else "Bollywood") to setOf(MovieLanguage.HINDI_MODERN, MovieLanguage.HINDI_CLASSIC),
                        (if (ne) "दक्षिण भारतीय" else "South Indian") to setOf(MovieLanguage.SOUTH_INDIAN))
                    choices.forEachIndexed { index, (title, languages) ->
                        FilterChip(selected = selectedLanguages == languages,
                            onClick = { onSelectCinema(languages) }, label = { Text(title) },
                            modifier = Modifier.testTag("home_cinema_$index"),
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ModernAmber,
                                selectedLabelColor = ModernBg, labelColor = TextPrimary))
                    }
                }
                CinemaButton(if (ne) "खेल सुरु गरौँ" else "Let’s play", onContinue, Modifier.fillMaxWidth().testTag("splash_continue_button"))
                CinemaButton(if (ne) "कसरी खेल्ने?" else "How to play", { showRules = true },
                    Modifier.fillMaxWidth().testTag("home_help_button"), secondary = true)
                Text(if (ne) "२ ग्याङ • नेपाली, बलिउड र दक्षिण भारतीय चलचित्र" else "2 gangs • Nepali, Bollywood & South cinema",
                    color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Text(if (ne) "सन्दीप परियारद्वारा निर्मित" else "Made by Sandeep Pariyar", color = TextMuted, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

