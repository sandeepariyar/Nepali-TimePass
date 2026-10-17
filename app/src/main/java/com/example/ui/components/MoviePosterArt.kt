package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.WikipediaPosterRepository
import com.example.model.Movie
import com.example.ui.theme.ModernIndigoLight

/** Same poster provider/cache as the existing app; Coil decodes to the actual card bounds. */
@Composable
fun MoviePosterArt(movie: Movie, modifier: Modifier = Modifier) {
    val posters by WikipediaPosterRepository.posterUrls.collectAsStateWithLifecycle()
    val url = posters[movie.id]?.takeIf { it.isNotBlank() } ?: movie.posterUrl.takeIf { it.isNotBlank() }
    val context = LocalContext.current
    val request = remember(url, context) { ImageRequest.Builder(context).data(url).crossfade(180).build() }
    LaunchedEffect(movie.id) { if (url == null) WikipediaPosterRepository.fetchPosterForMovie(movie) }
    Box(modifier, contentAlignment = Alignment.Center) {
        Icon(Icons.Default.Theaters, null, Modifier.size(68.dp), tint = ModernIndigoLight.copy(alpha = .28f))
        if (url != null) {
            // Same remembered request/cache key for both layers. Never stretch poster artwork.
            AsyncImage(request, null, Modifier.fillMaxSize().graphicsLayer { alpha = .18f }, contentScale = ContentScale.Crop)
            AsyncImage(request, null, Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
        }
    }
}
