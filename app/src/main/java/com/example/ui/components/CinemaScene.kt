package com.example.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.R
import com.example.model.Team
import com.example.ui.theme.*

/** Static art never owns an animation clock. All interactive content stays native. */
@Composable
fun CinemaScene(content: @Composable ColumnScope.() -> Unit) {
    CinemaBackdrop {

        Column(Modifier.widthIn(max = 600.dp).fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
    }
}

@Composable
fun CinemaBackdrop(content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(ModernBg), contentAlignment = Alignment.TopCenter) {
        Image(painterResource(R.drawable.cinema_courtyard), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Box(Modifier.fillMaxSize().background(ModernBg.copy(alpha = .40f)))
        content()
    }
}

@Composable
fun CinemaHeading(eyebrow: String, title: String, subtitle: String = "") {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(eyebrow, color = ModernAmber, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(title, color = TextPrimary, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center)
        if (subtitle.isNotBlank()) { Spacer(Modifier.height(8.dp)); Text(subtitle, color = TextSecondary, textAlign = TextAlign.Center) }
    }
}

@Composable
fun CinemaPanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier.fillMaxWidth().drawWithContent {
        drawContent()
        val brass = Color(0xFFF0BE71).copy(alpha = .60f)
        val inset = 6.dp.toPx()
        for (x in listOf(inset, size.width - inset)) for (y in listOf(inset, size.height - inset)) {
            val r = 2.5.dp.toPx()
            val jewel = Path().apply { moveTo(x, y-r); lineTo(x+r, y); lineTo(x, y+r); lineTo(x-r, y); close() }
            drawPath(jewel, brass)
        }
        val mid = size.width / 2
        drawLine(brass, Offset(mid - 18.dp.toPx(), inset), Offset(mid + 18.dp.toPx(), inset), 1.dp.toPx())
        drawCircle(brass, 2.dp.toPx(), Offset(mid, inset))
    }, shape = RoundedCornerShape(8.dp), color = ModernSurface.copy(alpha = .97f),
        border = BorderStroke(1.dp, ModernAmber.copy(alpha = .40f)), shadowElevation = 8.dp) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

@Composable
fun CinemaButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    secondary: Boolean = false, enabled: Boolean = true) {
    Button(onClick, modifier.defaultMinSize(minHeight = 54.dp), enabled = enabled,
        shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, ModernAmber.copy(alpha = .55f)),
        colors = ButtonDefaults.buttonColors(containerColor = if (secondary) ModernSurface else ModernAmber,
            contentColor = if (secondary) TextPrimary else ModernBg)) {
        Text(text, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 4.dp))
    }
}

@Composable
fun GangScore(team: Team, modifier: Modifier = Modifier, active: Boolean = false) {
    Surface(modifier, color = if (active) Color(team.colorHex).copy(alpha = .22f) else ModernSurface,
        shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, if (active) ModernAmber else ModernCardAlt)) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            TeamEmblem(team.id, Color(team.colorHex), size = 60.dp)
            Text(team.name, color = TextPrimary, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Text("${team.score}", style = MaterialTheme.typography.headlineLarge, color = ModernAmber)
        }
    }
}
