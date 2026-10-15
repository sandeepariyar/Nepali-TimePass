package com.example.data

import com.example.model.Movie
import com.example.model.MovieDifficulty
import com.example.model.MovieLanguage

object MovieDatabase {

    val movies: List<Movie> = ModernHindiMovies.list +
            ClassicHindiMovies.list +
            SouthIndianMovies.list +
            NepaliMovies.list

    fun getFilteredMovies(
        language: MovieLanguage = MovieLanguage.ALL,
        difficulty: MovieDifficulty? = null
    ): List<Movie> {
        return movies.filter { movie ->
            val langMatch = when (language) {
                MovieLanguage.ALL -> true
                MovieLanguage.HINDI_MODERN -> movie.language == MovieLanguage.HINDI_MODERN
                MovieLanguage.HINDI_CLASSIC -> movie.language == MovieLanguage.HINDI_CLASSIC
                MovieLanguage.SOUTH_INDIAN -> movie.language == MovieLanguage.SOUTH_INDIAN
                MovieLanguage.NEPALI -> movie.language == MovieLanguage.NEPALI
            }
            val diffMatch = difficulty == null || movie.difficulty == difficulty
            langMatch && diffMatch
        }
    }
}
