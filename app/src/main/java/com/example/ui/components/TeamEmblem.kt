package com.example.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import com.example.R

@Composable
fun TeamEmblem(teamId: Int, color: Color, modifier: Modifier = Modifier, size: Dp = 48.dp, iconSize: Dp = 24.dp) {
    Surface(modifier.size(size), shape = CircleShape, color = color.copy(alpha = .18f), border = BorderStroke(2.dp, color)) {
        Image(painterResource(if (teamId % 2 == 1) R.drawable.gang_lion else R.drawable.gang_panda), null,
            Modifier.fillMaxSize().padding(3.dp))
    }
}
