package com.example.data

import android.util.Log
import com.example.model.Movie
import com.example.model.MovieDifficulty
import com.example.model.MovieLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object OnlineFilmDirectory {
    private const val TAG = "OnlineFilmDir"

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val discoveredMovies = ConcurrentHashMap<String, Movie>()
    private val _onlineMovies = MutableStateFlow<List<Movie>>(emptyList())
    val onlineMovies: StateFlow<List<Movie>> = _onlineMovies.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private val _streamStatus = MutableStateFlow("🟢 Connected to Live Internet Movie Stream")
    val streamStatus: StateFlow<String> = _streamStatus.asStateFlow()

    // Category continuation tokens for infinite internet pagination
    private val continuationTokens = ConcurrentHashMap<String, String>()

    // Rich internet Wikipedia categories across all genres, years, and industries
    private val internetCategories = mapOf(
        MovieLanguage.HINDI_MODERN to listOf(
            "Category:2020s_Hindi-language_films",
            "Category:2010s_Hindi-language_films",
            "Category:2000s_Hindi-language_films",
            "Category:Hindi-language_action_films",
            "Category:Hindi-language_comedy_films",
            "Category:Hindi-language_romantic_films",
            "Category:Hindi-language_thriller_films",
            "Category:Bollywood_films",
            "Category:Indian_action_thriller_films"
        ),
        MovieLanguage.HINDI_CLASSIC to listOf(
            "Category:1990s_Hindi-language_films",
            "Category:1980s_Hindi-language_films",
            "Category:1970s_Hindi-language_films",
            "Category:1960s_Hindi-language_films",
            "Category:1950s_Hindi-language_films",
            "Category:Hindi-language_musical_films",
            "Category:Indian_epic_films"
        ),
        MovieLanguage.SOUTH_INDIAN to listOf(
            "Category:2020s_Tamil-language_films",
            "Category:2020s_Telugu-language_films",
            "Category:2020s_Malayalam-language_films",
            "Category:2020s_Kannada-language_films",
            "Category:2010s_Tamil-language_films",
            "Category:2010s_Telugu-language_films",
            "Category:2010s_Malayalam-language_films",
            "Category:2010s_Kannada-language_films",
            "Category:Tamil-language_action_films",
            "Category:Telugu-language_action_films",
            "Category:Malayalam-language_films",
            "Category:Kannada-language_films"
        ),
        MovieLanguage.NEPALI to listOf(
            "Category:Nepalese_films",
            "Category:2020s_Nepalese_films",
            "Category:2010s_Nepalese_films",
            "Category:2000s_Nepalese_films",
            "Category:1990s_Nepalese_films",
            "Category:1980s_Nepalese_films",
            "Category:Nepalese_comedy_films",
            "Category:Nepalese_drama_films",
            "Category:Nepalese_action_films"
        )
    )

    // Internet search queries for live discovery
    private val internetSearchSeeds = mapOf(
        MovieLanguage.HINDI_MODERN to listOf("Bollywood blockbuster film", "Hindi comedy film", "Hindi action movie", "Hindi romantic movie"),
        MovieLanguage.HINDI_CLASSIC to listOf("Vintage Bollywood film", "1970s Hindi movie", "1980s Hindi film", "1990s Bollywood film"),
        MovieLanguage.SOUTH_INDIAN to listOf("Telugu blockbuster film", "Tamil action film", "Malayalam hit movie", "Kannada cinema film"),
        MovieLanguage.NEPALI to listOf("Nepali hit film", "Nepalese cinema movie", "Kollywood Nepali film")
    )

    suspend fun fetchLimitlessOnlineFilms(
        category: MovieLanguage = MovieLanguage.ALL,
        forceMore: Boolean = false
    ): List<Movie> = withContext(Dispatchers.IO) {
        _isStreaming.value = true
        _streamStatus.value = "🔄 Fetching live movie stream from internet..."

        val targets = if (category == MovieLanguage.ALL) {
            listOf(
                MovieLanguage.HINDI_MODERN,
                MovieLanguage.HINDI_CLASSIC,
                MovieLanguage.SOUTH_INDIAN,
                MovieLanguage.NEPALI
            )
        } else {
            listOf(category)
        }

        var newAddedCount = 0

        for (lang in targets) {
            val catList = internetCategories[lang] ?: emptyList()
            // Pick multiple categories per language
            val selectedCats = if (forceMore) catList.shuffled().take(3) else catList.take(3)

            for (cat in selectedCats) {
                val films = fetchCategoryMembersWithContinuation(cat, lang)
                films.forEach { movie ->
                    if (!discoveredMovies.containsKey(movie.id)) {
                        discoveredMovies[movie.id] = movie
                        newAddedCount++
                    }
                }
            }

            // Also trigger a seed search if we need more
            val seedQueries = internetSearchSeeds[lang] ?: emptyList()
            if (seedQueries.isNotEmpty()) {
                val query = seedQueries.random()
                val searchFilms = searchInternetDirectly(query, lang)
                searchFilms.forEach { movie ->
                    if (!discoveredMovies.containsKey(movie.id)) {
                        discoveredMovies[movie.id] = movie
                        newAddedCount++
                    }
                }
            }
        }

        _onlineMovies.value = discoveredMovies.values.toList()
        _isStreaming.value = false
        _streamStatus.value = "🟢 Internet Stream Active: ${discoveredMovies.size} online movies fetched"
        Log.d(TAG, "Online movie pool expanded to ${discoveredMovies.size} titles (+$newAddedCount fresh)")
        return@withContext discoveredMovies.values.toList()
    }

    /**
     * Direct real-time Wikipedia search across the internet
     */
    suspend fun searchInternetDirectly(query: String, fallbackLang: MovieLanguage = MovieLanguage.ALL): List<Movie> = withContext(Dispatchers.IO) {
        val list = mutableListOf<Movie>()
        try {
            val encoded = URLEncoder.encode(query, "UTF-8")
            val url = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=$encoded&srlimit=25&format=json"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "MovieCharadesApp/2.0 (Android; open_charades@wikimedia.org) Mozilla/5.0")
                .header("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val body = response.body?.string() ?: return@withContext emptyList()
                val json = JSONObject(body)
                val searchArr = json.optJSONObject("query")?.optJSONArray("search") ?: return@withContext emptyList()

                for (i in 0 until searchArr.length()) {
                    val item = searchArr.getJSONObject(i)
                    val rawTitle = item.optString("title", "")
                    val snippet = item.optString("snippet", "")

                    if (rawTitle.isEmpty() || rawTitle.startsWith("List of") || rawTitle.contains("filmography", ignoreCase = true)) {
                        continue
                    }

                    // Check if it's a film
                    val isFilm = rawTitle.contains("(film)", ignoreCase = true) ||
                            rawTitle.contains("film", ignoreCase = true) ||
                            snippet.contains("directed by", ignoreCase = true) ||
                            snippet.contains("film", ignoreCase = true) ||
                            snippet.contains("starring", ignoreCase = true)

                    if (!isFilm) continue

                    val clean = cleanMovieTitle(rawTitle)
                    if (clean.length < 2) continue

                    val yearMatch = Regex("\\((\\d{4})").find(rawTitle)
                    val extractedYear = yearMatch?.groupValues?.getOrNull(1)?.toIntOrNull() ?: extractYearFromSnippet(snippet) ?: 2022

                    val detectedLang = when {
                        fallbackLang != MovieLanguage.ALL -> fallbackLang
                        snippet.contains("Telugu", ignoreCase = true) || snippet.contains("Tamil", ignoreCase = true) ||
                                snippet.contains("Malayalam", ignoreCase = true) || snippet.contains("Kannada", ignoreCase = true) -> MovieLanguage.SOUTH_INDIAN
                        snippet.contains("Nepali", ignoreCase = true) || snippet.contains("Nepalese", ignoreCase = true) -> MovieLanguage.NEPALI
                        extractedYear < 2000 -> MovieLanguage.HINDI_CLASSIC
                        else -> MovieLanguage.HINDI_MODERN
                    }

                    val id = "online_search_${clean.lowercase().replace(Regex("[^a-z0-9]"), "_")}_$extractedYear"
                    val movie = Movie(
                        id = id,
                        title = clean,
                        nativeTitle = "",
                        year = extractedYear,
                        language = detectedLang,
                        genre = detectGenreFromSnippet(snippet, detectedLang),
                        difficulty = if (extractedYear < 1990) MovieDifficulty.HARD else MovieDifficulty.MEDIUM,
                        actors = extractActorsFromSnippet(snippet)
                    )
                    list.add(movie)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Search direct failed: ${e.message}")
        }
        return@withContext list
    }

    private fun fetchCategoryMembersWithContinuation(
        categoryName: String,
        language: MovieLanguage
    ): List<Movie> {
        val list = mutableListOf<Movie>()
        try {
            val encodedCat = URLEncoder.encode(categoryName, "UTF-8")
            val token = continuationTokens[categoryName] ?: ""
            val continueParam = if (token.isNotEmpty()) "&cmcontinue=$token" else ""
            val url = "https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&cmtitle=$encodedCat&cmlimit=50&cmtype=page&format=json$continueParam"

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "MovieCharadesApp/2.0 (Android; open_charades@wikimedia.org) Mozilla/5.0")
                .header("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return emptyList()
                val body = response.body?.string() ?: return emptyList()
                val json = JSONObject(body)

                // Save next pagination token for limitless browsing
                val nextContinue = json.optJSONObject("continue")?.optString("cmcontinue", "") ?: ""
                if (nextContinue.isNotEmpty()) {
                    continuationTokens[categoryName] = nextContinue
                }

                val query = json.optJSONObject("query") ?: return emptyList()
                val members = query.optJSONArray("categorymembers") ?: return emptyList()

                for (i in 0 until members.length()) {
                    val item = members.getJSONObject(i)
                    val rawTitle逃 = item.optString("title", "")
                    val rawTitle = rawTitle逃
                    if (rawTitle.isEmpty() ||
                        rawTitle.startsWith("List of", ignoreCase = true) ||
                        rawTitle.startsWith("Template:", ignoreCase = true) ||
                        rawTitle.startsWith("Category:", ignoreCase = true) ||
                        rawTitle.startsWith("Cinema of", ignoreCase = true) ||
                        rawTitle.contains("filmography", ignoreCase = true) ||
                        rawTitle.contains("Award for", ignoreCase = true) ||
                        rawTitle.contains("Film Awards", ignoreCase = true) ||
                        rawTitle.contains("in film", ignoreCase = true) ||
                        rawTitle.contains("discography", ignoreCase = true)
                    ) {
                        continue
                    }

                    val clean = cleanMovieTitle(rawTitle)
                    if (clean.length < 2 || clean.equals("Nepali cinema", ignoreCase = true) || clean.equals("Cinema of Nepal", ignoreCase = true)) continue

                    // Extract year from raw title
                    val yearMatch = Regex("\\((\\d{4})").find(rawTitle)
                    val extractedYear = yearMatch?.groupValues?.getOrNull(1)?.toIntOrNull() ?: when (language) {
                        MovieLanguage.HINDI_CLASSIC -> (1970..1999).random()
                        MovieLanguage.HINDI_MODERN -> (2005..2024).random()
                        MovieLanguage.SOUTH_INDIAN -> (2015..2024).random()
                        MovieLanguage.NEPALI -> (2010..2024).random()
                        else -> 2022
                    }

                    val id = "wiki_${clean.lowercase().replace(Regex("[^a-z0-9]"), "_")}_$extractedYear"
                    val movie = Movie(
                        id = id,
                        title = clean,
                        nativeTitle = "",
                        year = extractedYear,
                        language = language,
                        genre = when (language) {
                            MovieLanguage.HINDI_CLASSIC -> "Classic Cinema"
                            MovieLanguage.HINDI_MODERN -> "Bollywood Blockbuster"
                            MovieLanguage.SOUTH_INDIAN -> "South Cinema Hit"
                            MovieLanguage.NEPALI -> "Nepali Cinema"
                            else -> "Cinema"
                        },
                        difficulty = if (extractedYear < 1985) MovieDifficulty.HARD else MovieDifficulty.MEDIUM,
                        posterUrl = ""
                    )
                    list.add(movie)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error fetching category $categoryName: ${e.message}")
        }
        return list
    }

    private fun cleanMovieTitle(rawTitle: String): String {
        return rawTitle
            .replace(Regex("\\s*\\(\\d{4}\\s*film\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(film\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(upcoming film\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(Nepali film\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(Hindi film\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(Tamil film\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(Telugu film\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(Kannada film\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(Malayalam film\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(.*film\\)", RegexOption.IGNORE_CASE), "")
            .trim()
    }

    private fun extractYearFromSnippet(snippet: String): Int? {
        val match = Regex("\\b(19\\d{2}|20\\d{2})\\b").find(snippet)
        return match?.groupValues?.getOrNull(1)?.toIntOrNull()
    }

    private fun detectGenreFromSnippet(snippet: String, language: MovieLanguage): String {
        return when {
            snippet.contains("comedy", ignoreCase = true) -> "Comedy"
            snippet.contains("action", ignoreCase = true) -> "Action"
            snippet.contains("romantic", ignoreCase = true) || snippet.contains("romance", ignoreCase = true) -> "Romantic"
            snippet.contains("thriller", ignoreCase = true) -> "Thriller"
            snippet.contains("drama", ignoreCase = true) -> "Drama"
            snippet.contains("horror", ignoreCase = true) -> "Horror"
            language == MovieLanguage.SOUTH_INDIAN -> "South Masala / Action"
            language == MovieLanguage.HINDI_CLASSIC -> "Classic Golden Era"
            language == MovieLanguage.NEPALI -> "Social Drama"
            else -> "Cinema"
        }
    }

    private fun extractActorsFromSnippet(snippet: String): String {
        val starringMatch = Regex("starring\\s+([^<.,;]+)", RegexOption.IGNORE_CASE).find(snippet)
        if (starringMatch != null) {
            val raw = starringMatch.groupValues.getOrNull(1)?.replace(Regex("<[^>]*>"), "")?.trim() ?: ""
            if (raw.isNotEmpty() && raw.length > 3) return raw
        }
        return ""
    }
}
