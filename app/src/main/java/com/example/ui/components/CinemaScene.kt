package com.example.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.model.Team
import com.example.ui.theme.*

/** Borderless native surfaces. Static screens never own an animation clock. */
@Composable
fun CinemaScene(content: @Composable ColumnScope.() -> Unit) {
    CinemaBackdrop {
        Column(Modifier.widthIn(max = 600.dp).fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp), content = content)
    }
}

@Composable
fun CinemaBackdrop(content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(ModernBg), contentAlignment = Alignment.TopCenter, content = content)
}

@Composable
fun CinemaHeading(eyebrow: String, title: String, subtitle: String = "") {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(eyebrow, color = ModernAmber, style = MaterialTheme.typography.labelLarge)
        Text(title, color = TextPrimary, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        if (subtitle.isNotBlank()) Text(subtitle, color = TextSecondary, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun CinemaPanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), color = ModernSurface) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp), content = content)
    }
}

@Composable
fun CinemaButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    secondary: Boolean = false, enabled: Boolean = true) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed && enabled) .97f else 1f,
        spring(dampingRatio = .78f, stiffness = 650f), label = "button_press")
    Button(onClick, modifier.graphicsLayer { scaleX = scale; scaleY = scale }.defaultMinSize(minHeight = 56.dp), enabled = enabled,
        interactionSource = interaction,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (secondary) ModernCard else ModernAmber,
            contentColor = if (secondary) TextPrimary else ModernBg,
            disabledContainerColor = ModernCard, disabledContentColor = TextMuted)) {
        Text(text, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 3.dp))
    }
}

@Composable
fun GangScore(team: Team, modifier: Modifier = Modifier, active: Boolean = false) {
    Surface(modifier, color = if (active) Color(team.colorHex).copy(alpha = .16f) else ModernSurface,
        shape = RoundedCornerShape(28.dp)) {
        Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            TeamEmblem(team.id, Color(team.colorHex), size = 56.dp)
            Text(team.name, color = TextPrimary, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium)
            AnimatedContent(targetState = team.score, label = "score_change") { score ->
                Text("$score", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = if (active) Color(team.colorHex) else TextPrimary)
            }
        }
    }
}
