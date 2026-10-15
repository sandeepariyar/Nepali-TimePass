package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ModernLightColorScheme = lightColorScheme(
    primary = ModernIndigo,
    onPrimary = Color.White,
    primaryContainer = ModernIndigoLight,
    onPrimaryContainer = ModernIndigoDeep,

    secondary = ModernCrimson,
    onSecondary = Color.White,
    secondaryContainer = ModernCrimsonLight,
    onSecondaryContainer = ModernCrimson,

    tertiary = ModernEmerald,
    onTertiary = Color.White,
    tertiaryContainer = ModernEmeraldLight,
    onTertiaryContainer = Color(0xFF065F46),

    background = ModernBg,
    onBackground = TextPrimary,

    surface = ModernSurface,
    onSurface = TextPrimary,
    surfaceVariant = ModernCardAlt,
    onSurfaceVariant = TextSecondary,

    outline = ModernBorder,
    outlineVariant = ModernBorderStrong
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = ModernBg.toArgb()
                window.navigationBarColor = ModernBg.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = ModernLightColorScheme,
        typography = Typography,
        content = content
    )
}

