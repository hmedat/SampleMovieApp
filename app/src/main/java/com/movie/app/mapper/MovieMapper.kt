package com.movie.app.mapper

import com.movie.app.modules.Movie

object MovieMapper {
    private const val IMAGE_URL = "http://image.tmdb.org/t/p/w185"

    fun map(list: List<Movie>) {
        for (movie in list) {
            map(movie)
        }
    }

    fun map(movie: Movie) {
        if (movie.posterPath != null) {
            movie.posterPath = IMAGE_URL + movie.posterPath
        }
        if (movie.backdropPath != null) {
            movie.backdropPath = IMAGE_URL + movie.backdropPath
        }
        if (movie.genres != null && movie.genres.isNotEmpty()) {
            val genresSize = Math.min(movie.genres.size, 3) - 1
            val genresStringBuilder = StringBuilder()
            genresStringBuilder.append(movie.genres[0].name)
            for (index in 1..genresSize) {
                genresStringBuilder.append(", ").append(movie.genres[index].name)
            }
            movie.genresString = genresStringBuilder.toString()
        }
    }
}
