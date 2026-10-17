package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlin.math.cos
import kotlin.math.sin

/** One bounded celebration around the badge; no idle animation or full-screen particles. */
@Composable
fun WinnerBadgeBurst(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var finished by remember { mutableStateOf(false) }
    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            if (!finished) {
                progress.animateTo(1f, tween(1600))
                finished = true
            }
        }
    }
    Canvas(modifier) {
        val t = progress.value
        if (t >= 1f) return@Canvas
        repeat(24) { i ->
            val angle = i * (Math.PI * 2 / 24)
            val radius = size.minDimension * (.23f + t * .25f)
            val point = center + Offset(cos(angle).toFloat() * radius, sin(angle).toFloat() * radius)
            val color = (if (i % 3 == 0) Color(0xFFFF365C) else Color(0xFFFFCE69)).copy(alpha = 1f - t)
            rotate(i * 31f + t * 120f, point) {
                drawRect(color, point - Offset(2.dp.toPx(), 2.dp.toPx()), androidx.compose.ui.geometry.Size(4.dp.toPx(), 4.dp.toPx()))
            }
        }
    }
}
