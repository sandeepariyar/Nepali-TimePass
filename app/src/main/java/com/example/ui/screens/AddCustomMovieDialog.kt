package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Movie
import com.example.model.MovieDifficulty
import com.example.model.MovieLanguage
import com.example.ui.theme.ModernBg
import com.example.ui.theme.ModernBg
import com.example.ui.theme.ModernCard
import com.example.ui.theme.ModernCrimson
import com.example.ui.theme.ModernEmerald
import com.example.ui.theme.ModernIndigo
import com.example.ui.theme.ModernIndigoLight
import com.example.ui.theme.ModernSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun AddCustomMovieDialog(
    onDismiss: () -> Unit,
    onAddCustomMovie: (Movie) -> Unit,
    isNepali: Boolean = false
) {
    var customTitle by remember { mutableStateOf("") }
    var customNativeTitle by remember { mutableStateOf("") }
    var customYear by remember { mutableStateOf("2024") }
    var customGenre by remember { mutableStateOf("Comedy / Drama") }
    var customActors by remember { mutableStateOf("") }
    var addSuccessMsg by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ModernSurface,
            modifier = Modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth(.92f)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = ModernCard,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalMovies,
                                contentDescription = null,
                                tint = ModernIndigoLight,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = (if (isNepali) "गोप्य चलचित्र थप्नुहोस्" else "Add Secret Movie"),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = (if (isNepali) "खेलको चलचित्र सूचीमा थपिन्छ" else "Added directly into mystery deck"),
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Privacy Note
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ModernCard,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = ModernCrimson,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = (if (isNepali) "यो चलचित्र पालोमा नआएसम्म गोप्य रहनेछ।" else "Anti-cheat: The movie will be kept confidential in the deck until drawn during a turn."),
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = customTitle,
                    onValueChange = { customTitle = it },
                    label = { Text((if (isNepali) "चलचित्रको नाम (अङ्ग्रेजी)" else "Movie Title (English)")) },
                    placeholder = { Text((if (isNepali) "जस्तै: Chhakka Panja" else "e.g. Chhakka Panja"), color = TextMuted) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_movie_title_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ModernIndigoLight,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = ModernCard,
                        unfocusedContainerColor = ModernCard,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = ModernIndigoLight,
                        unfocusedLabelColor = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = customNativeTitle,
                    onValueChange = { customNativeTitle = it },
                    label = { Text((if (isNepali) "देवनागरी नाम (नेपाली / हिन्दी)" else "Native Script (नेपाली / हिन्दी)")) },
                    placeholder = { Text((if (isNepali) "जस्तै: छक्का पन्जा" else "e.g. छक्का पन्जा"), color = TextMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ModernIndigoLight,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = ModernCard,
                        unfocusedContainerColor = ModernCard,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = ModernIndigoLight,
                        unfocusedLabelColor = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = customYear,
                        onValueChange = { customYear = it },
                        label = { Text((if (isNepali) "वर्ष" else "Year")) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ModernIndigoLight,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = ModernCard,
                            unfocusedContainerColor = ModernCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = ModernIndigoLight,
                            unfocusedLabelColor = TextSecondary
                        )
                    )
                    OutlinedTextField(
                        value = customGenre,
                        onValueChange = { customGenre = it },
                        label = { Text((if (isNepali) "विधा" else "Genre")) },
                        singleLine = true,
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ModernIndigoLight,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = ModernCard,
                            unfocusedContainerColor = ModernCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = ModernIndigoLight,
                            unfocusedLabelColor = TextSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = customActors,
                    onValueChange = { customActors = it },
                    label = { Text((if (isNepali) "कलाकार (ऐच्छिक)" else "Star Cast (Optional)")) },
                    placeholder = { Text((if (isNepali) "जस्तै: दीपकराज गिरी, प्रियङ्का कार्की" else "e.g. Deepak Raj Giri, Priyanka Karki"), color = TextMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ModernIndigoLight,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = ModernCard,
                        unfocusedContainerColor = ModernCard,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = ModernIndigoLight,
                        unfocusedLabelColor = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (addSuccessMsg.isNotEmpty()) {
                    Text(
                        text = addSuccessMsg,
                        color = ModernEmerald,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        if (customTitle.isNotBlank()) {
                            val yearInt = customYear.toIntOrNull() ?: 2024
                            val newMovie = Movie(
                                id = "custom_${System.currentTimeMillis()}",
                                title = customTitle.trim(),
                                nativeTitle = customNativeTitle.trim(),
                                year = yearInt,
                                language = MovieLanguage.NEPALI,
                                genre = customGenre.trim().ifEmpty { "Cinema" },
                                actors = customActors.trim(),
                                difficulty = MovieDifficulty.MEDIUM
                            )
                            onAddCustomMovie(newMovie)
                            addSuccessMsg = if (isNepali) "चलचित्र खेलमा थपियो!" else "Added '${newMovie.title}' to the secret deck!"
                            customTitle = ""
                            customNativeTitle = ""
                            customActors = ""
                        }
                    },
                    enabled = customTitle.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ModernIndigo,
                        contentColor = ModernBg
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_custom_movie_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = (if (isNepali) "खेलमा थप्नुहोस्" else "ADD TO GAME DECK"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

