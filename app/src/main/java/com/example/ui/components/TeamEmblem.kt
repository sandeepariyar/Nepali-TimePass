package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R

/** Shared illustrated identity across handoffs and score cards. Text stays native. */
@Suppress("UNUSED_PARAMETER")
@Composable
fun TeamEmblem(teamId: Int, color: Color, modifier: Modifier = Modifier, size: Dp = 48.dp, iconSize: Dp = 24.dp) {
    Image(painterResource(if (teamId % 2 == 1) R.drawable.gang_lightning else R.drawable.gang_flame),
        contentDescription = null, modifier = modifier.size(size), contentScale = ContentScale.Fit)
}
