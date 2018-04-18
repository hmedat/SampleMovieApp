package com.movie.app.mapper

import com.movie.app.modules.Movie

object MovieMapper {
    private const val IMAGE_URL = "http://image.tmdb.org/t/p/w185"

    fun map(list: List<Movie>) {
        for (movie in list) {
            if (movie.posterPath != null) {
                movie.posterPath = IMAGE_URL + movie.posterPath
            }
            if (movie.backdropPath != null) {
                movie.backdropPath = IMAGE_URL + movie.backdropPath
            }
        }
    }
}
