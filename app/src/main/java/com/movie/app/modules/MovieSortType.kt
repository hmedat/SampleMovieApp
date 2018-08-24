package com.movie.app.modules

enum class MovieSortType(private val displayName: String, val apiSearchName: String) {
    POPULARITY("Popularity", "popularity.desc"),
    RELEASE_DATE("Release Date", "release_date.desc");

    override fun toString(): String {
        return displayName
    }
}
