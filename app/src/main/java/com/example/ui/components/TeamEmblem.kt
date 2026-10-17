package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*

@Composable
fun TeamEmblem(teamId: Int, color: Color, modifier: Modifier = Modifier, size: Dp = 48.dp, iconSize: Dp = 24.dp) {
    Surface(modifier.size(size), shape = RoundedCornerShape(30), color = color.copy(alpha = .18f)) {
        Box(contentAlignment = Alignment.Center) {
            Icon(if (teamId % 2 == 1) Icons.Default.Bolt else Icons.Default.LocalFireDepartment,
                null, Modifier.size(size * .52f), tint = color)
        }
    }
}
