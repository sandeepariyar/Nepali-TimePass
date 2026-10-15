package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.model.AppLanguage
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun SplashScreen(appLanguage: AppLanguage = AppLanguage.NEPALI, onToggleLanguage: () -> Unit = {}, onContinue: () -> Unit) {
    val ne = appLanguage == AppLanguage.NEPALI
    CinemaScene {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("TIMEPASS", color = TextPrimary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelLarge)
            TextButton(onClick = onToggleLanguage, modifier = Modifier.testTag("welcome_language_button")) {
                Text(if (ne) "English / नेपाली" else "नेपाली / English", color = ModernAmber)
            }
        }
        Box(Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center) {
            Surface(Modifier.size(150.dp).rotate(-12f).offset(x = (-28).dp), color = ModernIndigo, shape = RoundedCornerShape(36.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Theaters, null, Modifier.size(76.dp), tint = ModernBg) }
            }
            Surface(Modifier.size(94.dp).rotate(12f).offset(x = 68.dp, y = 42.dp), color = ModernAmber, shape = RoundedCornerShape(28.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.PlayArrow, null, Modifier.size(58.dp), tint = ModernBg) }
            }
        }
        CinemaHeading(if (ne) "नेपाली पार्टी खेल" else "THE NEPALI PARTY GAME", if (ne) "अभिनय गरौँ।\nरमाइलो गरौँ।" else "Big acting.\nBigger laughs.",
            if (ne) "आफ्नो ग्याङसँग चलचित्र अनुमान गर्नुहोस्। बोल्न पाइँदैन, रमाइलो गर्न पाइन्छ।" else "Guess the film with your gang. No talking. All the drama.")
        CinemaButton(if (ne) "खेल सुरु गरौँ" else "Let’s play", onContinue, Modifier.fillMaxWidth().testTag("splash_continue_button"))
        Text(if (ne) "नेपाली • बलिउड • दक्षिण भारतीय चलचित्र" else "Nepali • Bollywood • South cinema", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
        Text(if (ne) "सन्दीप परियारद्वारा निर्मित" else "Made by Sandeep Pariyar", color = TextMuted, style = MaterialTheme.typography.bodySmall)
    }
}
