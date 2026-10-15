package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.isActive
import kotlin.math.*

/** The only continuous scene clock. Stops while paused, backgrounded or off-screen. */
@Composable
fun StageLighting(remaining: Int, total: Int, running: Boolean, modifier: Modifier = Modifier) {
    val owner = LocalLifecycleOwner.current
    var phase by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(running, owner) {
        if (running) owner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            var previous = 0L
            while (isActive) withFrameNanos { frame ->
                if (previous != 0L) phase += ((frame - previous).coerceAtMost(50_000_000) / 1_000_000_000f)
                previous = frame
            }
        }
    }
    Canvas(modifier) {
        val p = phase // State is read in draw, avoiding recomposition of movie and poster.
        val urgent = remaining <= 10
        val progress = remaining.toFloat() / total.coerceAtLeast(1)
        val warm = Color(0xFFB7F36B)
        val accent = if (urgent) Color(0xFFFF6577) else warm
        val glow = if (urgent) .14f + .12f * (sin(p * 5f) + 1f) / 2f else .07f
        drawRect(Brush.radialGradient(listOf(accent.copy(alpha = glow), Color.Transparent), center = Offset(size.width / 2, size.height * .30f), radius = size.maxDimension * .7f))
        // Marquee bulbs extinguish like a theatre closing, rather than a conventional timer ring.
        val count = 24
        for (i in 0 until count) {
            val x = (i + .5f) / count * size.width
            val on = i.toFloat() / count < progress
            val a = if (on) .85f + .15f * sin(p * 2 + i) else .10f
            for (y in listOf(12f, size.height - 12f)) {
                if (on) drawCircle(accent.copy(alpha = .08f), 10f, Offset(x, y))
                drawCircle(accent.copy(alpha = a.coerceIn(0f, 1f)), 3f, Offset(x, y))
            }
        }
        if (urgent) {
            // Slow ambient movement; no fast flashing or full-screen luminance changes.
            repeat(12) { i ->
                val x = size.width * ((i * .083f + sin(p * .3f + i) * .025f + 1f) % 1f)
                val y = size.height * (1f - ((p * .07f + i * .083f) % 1f))
                drawCircle(warm.copy(alpha = .22f), 2f, Offset(x, y))
            }
        }
    }
}
