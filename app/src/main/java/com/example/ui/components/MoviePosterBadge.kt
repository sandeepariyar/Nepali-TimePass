package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.data.WikipediaPosterRepository
import com.example.model.Movie
import com.example.model.MovieLanguage
import com.example.ui.theme.CinemaGoldBright

@Composable
fun MoviePosterBadge(
    movie: Movie,
    modifier: Modifier = Modifier,
    width: Dp = 110.dp,
    height: Dp = 160.dp,
    cornerRadius: Dp = 14.dp
) {
    val posterMap by WikipediaPosterRepository.posterUrls.collectAsState()
    val posterUrl = posterMap[movie.id]?.takeIf { it.isNotEmpty() } ?: movie.posterUrl.takeIf { it.isNotEmpty() }

    LaunchedEffect(movie.id) {
        if (posterMap[movie.id] == null) {
            WikipediaPosterRepository.fetchPosterForMovie(movie)
        }
    }

    val context = LocalContext.current
    val imageRequest = remember(posterUrl, context) {
        ImageRequest.Builder(context).data(posterUrl)
            .addHeader("User-Agent", "MovieCharadesApp/1.0 (Android; charades_game@gmail.com) Mozilla/5.0")
            .crossfade(200).build()
    }
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .size(width = width, height = height)
            .shadow(4.dp, shape)
            .clip(shape)
            .background(Color(0xFF1E2942)),
        contentAlignment = Alignment.Center
    ) {
        if (!posterUrl.isNullOrEmpty()) {
            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = "${movie.title} Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                val state = painter.state
                when (state) {
                    is AsyncImagePainter.State.Success -> {
                        SubcomposeAsyncImageContent(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        // Instant stylized fallback card so there's never buffering or blank screen!
                        StylizedFallbackPoster(movie = movie)
                    }
                }
            }
        } else {
            StylizedFallbackPoster(movie = movie)
        }
    }
}

@Composable
private fun StylizedFallbackPoster(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    val gradientColors = when (movie.language) {
        MovieLanguage.HINDI_MODERN -> listOf(Color(0xFF381A54), Color(0xFF1E1030), Color(0xFF12081C))
        MovieLanguage.HINDI_CLASSIC -> listOf(Color(0xFF422817), Color(0xFF24150C), Color(0xFF140B06))
        MovieLanguage.SOUTH_INDIAN -> listOf(Color(0xFF4A1F0A), Color(0xFF281105), Color(0xFF130702))
        MovieLanguage.NEPALI -> listOf(Color(0xFF5E1714), Color(0xFF300C0A), Color(0xFF170605))
        MovieLanguage.ALL -> listOf(Color(0xFF2D1847), Color(0xFF170C24), Color(0xFF0D0614))
    }

    val accentColor = when (movie.language) {
        MovieLanguage.HINDI_MODERN -> CinemaGoldBright
        MovieLanguage.HINDI_CLASSIC -> Color(0xFFFFB74D)
        MovieLanguage.SOUTH_INDIAN -> Color(0xFFFF9800)
        MovieLanguage.NEPALI -> Color(0xFFFF8A65)
        MovieLanguage.ALL -> CinemaGoldBright
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header tag
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(accentColor.copy(alpha = 0.25f))
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${movie.language.flagEmoji} ${movie.year}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }

            // Cinema clapper icon
            Icon(
                imageVector = Icons.Default.Movie,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(32.dp)
            )

            // Title & native title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = movie.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 13.sp
                )
                if (movie.nativeTitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = movie.nativeTitle,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = accentColor.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

