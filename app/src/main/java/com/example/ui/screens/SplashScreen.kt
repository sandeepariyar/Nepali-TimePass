package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun SplashScreen(onContinue: () -> Unit) {
    CinemaScene {
        Spacer(Modifier.height(100.dp))
        CinemaHeading("KATHMANDU AFTER DARK", "नेपाली\nटाइमपास", "YOUR GANG. YOUR STAGE.\nA little drama. A lot of laughter.")
        Spacer(Modifier.height(30.dp))
        CinemaPanel {
            Text("फिल्म चिन्नुहोस्। अभिनय देखाउनुहोस्।", color = TextPrimary, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text("Nepali cinema • Bollywood • South cinema", color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            CinemaButton("खेलौँ • LET’S PLAY", onContinue, Modifier.fillMaxWidth().testTag("splash_continue_button"))
        }
        Text("Nepali TimePass by Sandeep Pariyar\nसंसारभरिका नेपालीहरूलाई समर्पित", color = TextSecondary, textAlign = TextAlign.Center)
    }
}
