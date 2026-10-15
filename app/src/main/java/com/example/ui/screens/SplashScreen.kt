package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.R
import com.example.model.AppLanguage
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun SplashScreen(appLanguage: AppLanguage = AppLanguage.NEPALI, onToggleLanguage: () -> Unit = {}, onContinue: () -> Unit) {
    val ne = appLanguage == AppLanguage.NEPALI
    var entered by remember { mutableStateOf(false) }
    val reveal by animateFloatAsState(if (entered) 1f else 0f, tween(360), label = "home_entrance")
    LaunchedEffect(Unit) { entered = true }
    CinemaBackdrop {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.widthIn(max = 600.dp).fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("TIMEPASS", color = TextPrimary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = onToggleLanguage, modifier = Modifier.testTag("welcome_language_button")) {
                        Text(if (ne) "English / नेपाली" else "नेपाली / English", color = ModernAmber)
                    }
                }
                Column(Modifier.graphicsLayer { alpha = reveal; translationY = (1f - reveal) * 16.dp.toPx() }) {
                    Text(if (ne) "तपाईंको ग्याङ।\nतपाईंको फिल्म।" else "Your gang.\nYour movie.",
                        color = TextPrimary, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                    Text(if (ne) "तपाईंको पल।" else "Your moment.", color = ModernAmber,
                        style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                    Text(if (ne) "अभिनय गरौँ, रमाइलो गरौँ" else "Act it out. Make memories.", color = ModernIndigoLight, style = MaterialTheme.typography.bodyMedium)
                }
                Image(painterResource(R.drawable.home_hero), contentDescription = null,
                    contentScale = ContentScale.Crop, alignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1.5f).graphicsLayer { alpha = reveal })
                CinemaButton(if (ne) "खेल सुरु गरौँ" else "Let’s play", onContinue, Modifier.fillMaxWidth().testTag("splash_continue_button"))
                Text(if (ne) "२ ग्याङ • नेपाली, बलिउड र दक्षिण भारतीय चलचित्र" else "2 gangs • Nepali, Bollywood & South cinema",
                    color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Text(if (ne) "सन्दीप परियारद्वारा निर्मित" else "Made by Sandeep Pariyar", color = TextMuted, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
