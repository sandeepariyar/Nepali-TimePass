package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.data.WikipediaPosterRepository
import com.example.model.AppLanguage
import com.example.model.Movie
import com.example.model.MovieLanguage
import com.example.ui.theme.ModernAmber
import com.example.ui.theme.ModernCard
import com.example.ui.theme.ModernCardAlt
import com.example.ui.theme.ModernIndigo
import com.example.ui.theme.ModernSurface
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun MovieCard(
    movie: Movie,
    isCardRevealed: Boolean,
    onToggleReveal: () -> Unit,
    onSkip: () -> Unit = {},
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val isNepali = appLanguage == AppLanguage.NEPALI

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("movie_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ModernSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ModernSurface)
        ) {
            if (!isCardRevealed) {
                // ==================== HIDDEN / SECRET STATE ====================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onToggleReveal() }
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                        .testTag("hidden_card_overlay"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Modern Flat Reveal Icon
                    Surface(
                        shape = CircleShape,
                        color = ModernIndigo,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Reveal Card",
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = if (isNepali) "गोप्य चलचित्र लुकाइएको छ" else "Secret Movie Hidden",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isNepali)
                            "कलाकारले मात्र हेर्नुहोस्\nशीर्षक, पोस्टर र संकेत हेर्न छुनुहोस्"
                        else
                            "Only the ACTOR should look\nTap anywhere to reveal poster & title",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = ModernCardAlt
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = ModernIndigo,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isNepali) "हेर्न यहाँ थिच्नुहोस्" else "TAP TO REVEAL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            } else {
                // ==================== REVEALED / UNHIDDEN STATE ====================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // TOP BAR: Category Pill & Actions (Hide / Skip)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category / Genre / Year Chip
                        val genreYearText = buildString {
                            if (movie.language == MovieLanguage.NEPALI) append(if (isNepali) "नेपाली सिनेमा" else "NEPALI")
                            else if (movie.language == MovieLanguage.SOUTH_INDIAN) append(if (isNepali) "साउथ सिनेमा" else "SOUTH CINEMA")
                            else append(if (isNepali) "बलिउड" else "BOLLYWOOD")
                            if (movie.year > 0) append(" • ${movie.year}")
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = ModernIndigo.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = genreYearText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC7D2FE),
                                letterSpacing = 0.3.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Quick Hide Button
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = ModernCardAlt,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable { onToggleReveal() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VisibilityOff,
                                        contentDescription = "Hide",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isNepali) "लुकाउनुहोस्" else "Hide",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextSecondary
                                    )
                                }
                            }

                            // Skip action pill
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = ModernCardAlt,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable { onSkip() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FastForward,
                                        contentDescription = "Skip",
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isNepali) "छोड्नुहोस्" else "Skip",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    // CENTER: DEDICATED MOVIE POSTER BOX
                    MoviePosterBox(
                        movie = movie,
                        isNepali = isNepali
                    )

                    // MOVIE TITLE
                    val displayTitle = if (isNepali) {
                        if (movie.nativeTitle.isNotEmpty()) movie.nativeTitle else movie.title
                    } else {
                        movie.title
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = displayTitle,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.testTag("movie_title_text")
                        )

                        if (movie.genre.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = movie.genre,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeonPink,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // BOTTOM HINT / CAST CLUE CARD
                    val hintText = if (isNepali) {
                        when {
                            movie.actors.isNotEmpty() -> "कलाकार: ${movie.actors}"
                            movie.hint.isNotEmpty() -> "संकेत: ${movie.hint}"
                            movie.actingTip.isNotEmpty() -> "संकेत: ${movie.actingTip}"
                            else -> "संकेत: इशाराले अभिनय गरी टोलीलाई बुझाउनुहोस्!"
                        }
                    } else {
                        when {
                            movie.actors.isNotEmpty() -> "Cast: ${movie.actors}"
                            movie.hint.isNotEmpty() -> "Hint: ${movie.hint}"
                            movie.actingTip.isNotEmpty() -> "Tip: ${movie.actingTip}"
                            else -> "Hint: Act out syllables or iconic scenes!"
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ModernCard,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = ModernAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = hintText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoviePosterBox(
    movie: Movie,
    isNepali: Boolean
) {
    val posterMap by WikipediaPosterRepository.posterUrls.collectAsState()
    val posterUrl = posterMap[movie.id]?.takeIf { it.isNotEmpty() } ?: movie.posterUrl.takeIf { it.isNotEmpty() }

    LaunchedEffect(movie.id) {
        if (posterMap[movie.id] == null) {
            WikipediaPosterRepository.fetchPosterForMovie(movie)
        }
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ModernCard,
        modifier = Modifier
            .width(130.dp)
            .height(170.dp)
            .clip(RoundedCornerShape(18.dp))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (!posterUrl.isNullOrEmpty()) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(posterUrl)
                        .addHeader("User-Agent", "MovieCharadesApp/1.0 (Android; charades_game@gmail.com) Mozilla/5.0")
                        .crossfade(200)
                        .build(),
                    contentDescription = movie.title,
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
                            StylizedFallbackPosterBox(movie = movie, isNepali = isNepali)
                        }
                    }
                }
            } else {
                StylizedFallbackPosterBox(movie = movie, isNepali = isNepali)
            }
        }
    }
}

@Composable
private fun StylizedFallbackPosterBox(
    movie: Movie,
    isNepali: Boolean
) {
    val posterGradient = when (movie.language) {
        MovieLanguage.NEPALI -> listOf(Color(0xFF4C1D2A), Color(0xFF1E1017))
        MovieLanguage.HINDI_MODERN -> listOf(Color(0xFF1E2648), Color(0xFF10162B))
        MovieLanguage.HINDI_CLASSIC -> listOf(Color(0xFF3D2E14), Color(0xFF1C1407))
        MovieLanguage.SOUTH_INDIAN -> listOf(Color(0xFF422111), Color(0xFF1F0E05))
        MovieLanguage.ALL -> listOf(Color(0xFF2E1C47), Color(0xFF150C22))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(posterGradient))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Top flag & year badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.Black.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = movie.language.flagEmoji,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White.copy(alpha = 0.9f),
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                if (movie.year > 0) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.Black.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = "${movie.year}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Center Cinema Icon
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.08f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Theaters,
                        contentDescription = null,
                        tint = ModernAmber,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Bottom Cinema Label Pill
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.Black.copy(alpha = 0.4f)
            ) {
                Text(
                    text = if (isNepali) "सिनेमा" else "CINEMA",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
        }
    }
}
