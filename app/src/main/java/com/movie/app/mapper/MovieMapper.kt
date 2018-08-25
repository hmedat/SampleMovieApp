package com.movie.app.mapper

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie

object MovieMapper {
    private const val IMAGE_URL = "http://image.tmdb.org/t/p/w185"

    fun map(result: MoviesResult?): MoviesResult? {
        result?.results?.forEach {
            map(it)
        }
        return result
    }

    fun map(movie: Movie): Movie {
        return movie.apply {
            posterPath?.let {
                posterPath = IMAGE_URL + it
            }
            backdropPath?.let {
                backdropPath = IMAGE_URL + it
            }
            videosList = videoResult?.videos
        }
    }
}
