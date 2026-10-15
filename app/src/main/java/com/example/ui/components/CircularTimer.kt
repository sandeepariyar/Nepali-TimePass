package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.ModernCard
import com.example.ui.theme.ModernSurface
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WrongRed
import java.util.Locale

@Composable
fun CompactTimerButton(
    totalSeconds: Int,
    remainingSeconds: Int,
    isPaused: Boolean,
    onTogglePause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (totalSeconds > 0) {
        remainingSeconds.toFloat() / totalSeconds.toFloat()
    } else 0f

    val isUrgent = remainingSeconds <= 10 && remainingSeconds > 0 && !isPaused

    val targetColor = when {
        remainingSeconds > (totalSeconds * 0.4) -> CorrectGreen
        remainingSeconds > 15 -> WarningAmber
        else -> WrongRed
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "compact_timer_color"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_anim")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isUrgent) 1.06f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Surface(
        shape = RoundedCornerShape(50),
        color = if (isUrgent) WrongRed.copy(alpha = 0.22f) else ModernCard,
        modifier = modifier
            .scale(pulseScale)
            .clickable { onTogglePause() }
            .testTag("compact_timer_button")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Mini Canvas Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(24.dp)
            ) {
                Canvas(modifier = Modifier.size(24.dp)) {
                    val stroke = 3.dp.toPx()
                    drawCircle(
                        color = Color(0xFF273554),
                        style = Stroke(width = stroke)
                    )
                    drawArc(
                        color = animatedColor,
                        startAngle = -90f,
                        sweepAngle = progress * 360f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }
                Icon(
                    imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = if (isPaused) "Resume" else "Pause",
                    tint = animatedColor,
                    modifier = Modifier.size(12.dp)
                )
            }

            // Time text
            Text(
                text = timeFormatted,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = if (isUrgent) WrongRed else Color.White,
                modifier = Modifier.testTag("timer_text")
            )

            // Status label
            if (isPaused) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = WarningAmber.copy(alpha = 0.22f)
                ) {
                    Text(
                        text = "PAUSED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarningAmber,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CircularTimer(
    totalSeconds: Int,
    remainingSeconds: Int,
    isPaused: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    strokeWidth: Dp = 12.dp
) {
    val progress = if (totalSeconds > 0) {
        remainingSeconds.toFloat() / totalSeconds.toFloat()
    } else 0f

    val isUrgent = remainingSeconds <= 10 && remainingSeconds > 0 && !isPaused

    val targetColor = when {
        remainingSeconds > (totalSeconds * 0.4) -> CorrectGreen
        remainingSeconds > 15 -> WarningAmber
        else -> WrongRed
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "timer_color"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_anim")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isUrgent) 1.08f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .testTag("circular_timer")
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = strokeWidth.toPx()

            // Background ring track
            drawCircle(
                color = Color(0xFF1E2942),
                style = Stroke(width = stroke)
            )

            // Progress Arc
            drawArc(
                color = animatedColor,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeFormatted,
                fontSize = (26 * pulseScale).sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = if (isUrgent) WrongRed else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.testTag("timer_text")
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (isPaused) "PAUSED" else if (isUrgent) "HURRY UP!" else "REMAINING",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = if (isPaused) WarningAmber else if (isUrgent) WrongRed else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

