package com.movie.app.util

import com.movie.app.modules.Genre

object GenreUtil {

    fun getGenreString(genres: List<Genre>?): String {
        if (genres == null || genres.isEmpty()) {
            return ""
        }
        val genresSize = Math.min(genres.size, 3) - 1
        val genresStringBuilder = StringBuilder()
        genresStringBuilder.append(genres[0].name)
        for (index in 1..genresSize) {
            genresStringBuilder.append(", ").append(genres[index].name)
        }
        return genresStringBuilder.toString()
    }

}
