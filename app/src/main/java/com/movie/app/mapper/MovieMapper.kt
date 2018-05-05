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
            posterPath = IMAGE_URL + posterPath
            backdropPath = IMAGE_URL + backdropPath

            if (videoResult != null && videoResult?.videos != null && videoResult?.videos!!.isNotEmpty()) {
                val key = videoResult?.videos!![0].key
                firstVideoImageUrl = "http://img.youtube.com/vi/$key/0.jpg"
                firstVideoUrl = "https://www.youtube.com/watch?v=$key"
            }
            if (genres == null || genres!!.isEmpty()) {
                return
            }
            val genresSize = Math.min(genres!!.size, 3) - 1
            val genresStringBuilder = StringBuilder()
            genresStringBuilder.append(genres!![0].name)
            for (index in 1..genresSize) {
                genresStringBuilder.append(", ").append(genres!![index].name)
            }
            genresString = genresStringBuilder.toString()
        }
    }
}
