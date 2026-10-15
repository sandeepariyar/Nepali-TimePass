package com.example.model

enum class GamePhase {
    SPLASH,          // Animated welcome & poster cache preloader
    SETUP,           // Choosing gangs, gang colors, timer duration, language mix
    READY_TURN,      // "Team X turn! Pass phone to the actor"
    ACTING,          // Active countdown, movie revealed to actor, acting & guessing
    ROUND_SUMMARY,   // "Team X got it right! +1 Point" or "Time Out!"
    WINNER_PAGE      // Game concluded or target score reached -> Trophy, fireworks, stats
}

enum class AppLanguage {
    ENGLISH,
    NEPALI
}

data class GangColor(
    val id: String,
    val name: String,
    val nameNepali: String,
    val colorHex: Long,
    val lightHex: Long
)

object GangColorPalette {
    val COLORS = listOf(
        GangColor("crimson", "Crimson", "रातो", 0xFFE11D48, 0xFFFFE4E6),
        GangColor("blue", "Royal Blue", "नीलो", 0xFF2563EB, 0xFFDBEAFE),
        GangColor("emerald", "Emerald", "हरियो", 0xFF16A34A, 0xFFD1FAE5),
        GangColor("amber", "Amber Gold", "सुन्तला", 0xFFD97706, 0xFFFEF3C7),
        GangColor("violet", "Purple", "बैजनी", 0xFF7C3AED, 0xFFEDE9FE),
        GangColor("pink", "Hot Pink", "गुलाबी", 0xFFDB2777, 0xFFFCE7F3),
        GangColor("cyan", "Teal Cyan", "फिरोजी", 0xFF0891B2, 0xFFCFFAFE),
        GangColor("brown", "Rust Brown", "खैरो", 0xFF9A3412, 0xFFFFEDD5)
    )

    fun getColor(colorHex: Long): GangColor {
        return COLORS.find { it.colorHex == colorHex } ?: COLORS[0]
    }
}

data class Team(
    val id: Int,
    val name: String,
    val colorHex: Long,
    val emoji: String,
    var score: Int = 0,
    var correctCount: Int = 0,
    var passedCount: Int = 0,
    var timeOutCount: Int = 0
)

data class RoundRecord(
    val roundNumber: Int,
    val teamName: String,
    val teamColor: Long,
    val movie: Movie,
    val isCorrect: Boolean,
    val timeUsedSeconds: Int
)

data class GameSettings(
    val timerDurationSeconds: Int = 180, // Default 3 minutes (180s)
    val selectedLanguages: Set<MovieLanguage> = setOf(
        MovieLanguage.NEPALI,
        MovieLanguage.HINDI_MODERN,
        MovieLanguage.SOUTH_INDIAN
    ),
    val selectedDifficulty: MovieDifficulty? = null,
    val soundEffectsEnabled: Boolean = true,
    val targetScore: Int = 0,
    val totalRoundsPerTeam: Int = 0
) {
    val selectedLanguage: MovieLanguage
        get() = if (selectedLanguages.size == 1) selectedLanguages.first() else MovieLanguage.ALL
}

