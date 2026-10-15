package com.example.model

enum class MovieLanguage(val displayName: String, val flagEmoji: String, val description: String) {
    ALL("Blockbusters Mix", "ALL", "All-Time Mega Blockbusters: Nepali, Bollywood & South Cinema Mix"),
    NEPALI("Nepali Cinema", "NEP", "From Loot & Kabaddi to Chhakka Panja & Jaari"),
    HINDI_MODERN("Modern Bollywood", "BOL", "From 3 Idiots to Stree 2, Jawan & Animal"),
    HINDI_CLASSIC("Classic Bollywood", "CLS", "Vintage Classics: Sholay, DDLJ, Mughal-e-Azam"),
    SOUTH_INDIAN("South Cinema", "SOU", "Tamil, Telugu, Kannada & Malayalam (RRR, KGF, Pushpa)")
}

enum class MovieDifficulty(val label: String, val colorHex: Long) {
    EASY("Easy", 0xFF4CAF50),
    MEDIUM("Medium", 0xFFFF9800),
    HARD("Challenging", 0xFFE91E63)
}

data class Movie(
    val id: String,
    val title: String,
    val nativeTitle: String = "",
    val year: Int,
    val language: MovieLanguage,
    val genre: String,
    val difficulty: MovieDifficulty = MovieDifficulty.MEDIUM,
    val posterUrl: String = "",
    val actors: String = "",
    val hint: String = "",
    val actingTip: String = ""
)
