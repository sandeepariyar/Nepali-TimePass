package com.example.data

import com.example.model.Movie
import com.example.model.MovieDifficulty
import com.example.model.MovieLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class MovieRepository {

    private val _customMovies = MutableStateFlow<List<Movie>>(emptyList())
    val customMovies: StateFlow<List<Movie>> = _customMovies.asStateFlow()

    fun getAllMovies(
        language: MovieLanguage = MovieLanguage.ALL,
        difficulty: MovieDifficulty? = null
    ): List<Movie> {
        val languages = if (language == MovieLanguage.ALL) {
            setOf(MovieLanguage.NEPALI, MovieLanguage.HINDI_MODERN, MovieLanguage.HINDI_CLASSIC, MovieLanguage.SOUTH_INDIAN)
        } else {
            setOf(language)
        }
        return getMoviesForLanguages(languages, difficulty)
    }

    fun getMoviesForLanguages(
        languages: Set<MovieLanguage>,
        difficulty: MovieDifficulty? = null
    ): List<Movie> {
        val effectiveLanguages = if (languages.contains(MovieLanguage.ALL) || languages.isEmpty()) {
            setOf(MovieLanguage.NEPALI, MovieLanguage.HINDI_MODERN, MovieLanguage.HINDI_CLASSIC, MovieLanguage.SOUTH_INDIAN)
        } else {
            languages
        }

        val staticMovies = MovieDatabase.movies.filter { movie ->
            val langMatch = effectiveLanguages.contains(movie.language) ||
                    (effectiveLanguages.contains(MovieLanguage.HINDI_MODERN) && movie.language == MovieLanguage.HINDI_CLASSIC)
            val diffMatch = difficulty == null || movie.difficulty == difficulty
            langMatch && diffMatch
        }

        val onlineList = OnlineFilmDirectory.onlineMovies.value.filter { movie ->
            val langMatch = effectiveLanguages.contains(movie.language) ||
                    (effectiveLanguages.contains(MovieLanguage.HINDI_MODERN) && movie.language == MovieLanguage.HINDI_CLASSIC)
            val diffMatch = difficulty == null || movie.difficulty == difficulty
            langMatch && diffMatch
        }

        val filteredCustom = _customMovies.value.filter { movie ->
            val langMatch = effectiveLanguages.contains(movie.language) ||
                    (effectiveLanguages.contains(MovieLanguage.HINDI_MODERN) && movie.language == MovieLanguage.HINDI_CLASSIC)
            val diffMatch = difficulty == null || movie.difficulty == difficulty
            langMatch && diffMatch
        }

        // Merge without duplicates by ID or title
        val combinedMap = linkedMapOf<String, Movie>()
        staticMovies.forEach { combinedMap[it.id] = it }
        onlineList.forEach { combinedMap[it.id] = it }
        filteredCustom.forEach { combinedMap[it.id] = it }

        return combinedMap.values.toList()
    }

    suspend fun fetchLimitlessOnline(
        category: MovieLanguage = MovieLanguage.ALL,
        forceMore: Boolean = false
    ): List<Movie> = withContext(Dispatchers.IO) {
        OnlineFilmDirectory.fetchLimitlessOnlineFilms(category, forceMore)
        return@withContext getAllMovies(category)
    }

    suspend fun searchOnlineDirectly(
        query: String,
        category: MovieLanguage = MovieLanguage.ALL
    ): List<Movie> = withContext(Dispatchers.IO) {
        val results = OnlineFilmDirectory.searchInternetDirectly(query, category)
        return@withContext results
    }

    fun addCustomMovie(movie: Movie) {
        _customMovies.value = _customMovies.value + movie
    }
}

