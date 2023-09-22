package com.movie.app.util

import com.movie.app.modules.Genre

object GenreUtil {
    private const val GENRES_SIZE = 3

    fun getGenreString(genres: List<Genre>?): String {
        val list = genres?.subList(0, Math.min(genres.size, GENRES_SIZE)) ?: return ""
        return list.joinToString(", ") { it.name ?: "" }
    }
}
