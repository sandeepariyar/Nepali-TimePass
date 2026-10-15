package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

private data class Particle(
    val xRatio: Float,
    val initialY: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float,
    val wobbleSpeed: Float
)

@Composable
fun ConfettiEffect(
    modifier: Modifier = Modifier,
    particleCount: Int = 70
) {
    val progress = remember { Animatable(0f) }

    val colors = listOf(
        Color(0xFFFFD700), // Gold
        Color(0xFFFF3366), // Red / Pink
        Color(0xFF00E676), // Green
        Color(0xFF00B0FF), // Blue
        Color(0xFFFF9100), // Orange
        Color(0xFFE040FB), // Purple
        Color(0xFFFFFFFF)  // White sparkle
    )

    val particles = remember {
        List(particleCount) {
            Particle(
                xRatio = Random.nextFloat(),
                initialY = -Random.nextFloat() * 400f,
                speed = 600f + Random.nextFloat() * 800f,
                size = 12f + Random.nextFloat() * 16f,
                color = colors[Random.nextInt(colors.size)],
                rotationSpeed = 180f + Random.nextFloat() * 360f,
                wobbleSpeed = 2f + Random.nextFloat() * 4f
            )
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1400, easing = LinearEasing)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val t = progress.value
        if (t >= 1f) return@Canvas

        particles.forEach { p ->
            val y = p.initialY + (p.speed * t)
            if (y < height + 50) {
                val wobble = kotlin.math.sin((t * p.wobbleSpeed * Math.PI * 2).toFloat()) * 30f
                val x = (p.xRatio * width) + wobble
                val rotation = t * p.rotationSpeed

                rotate(degrees = rotation, pivot = Offset(x, y)) {
                    drawRect(
                        color = p.color,
                        topLeft = Offset(x - p.size / 2, y - p.size / 4),
                        size = Size(p.size, p.size * 0.6f)
                    )
                }
            }
        }
    }
}

