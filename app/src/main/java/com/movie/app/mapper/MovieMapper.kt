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
        movie.apply {
            posterPath = posterPath?.let {
                IMAGE_URL + it
            }
            backdropPath = backdropPath?.let {
                IMAGE_URL + it
            }
            videosList = videoResult?.videos
        }
    }
}
