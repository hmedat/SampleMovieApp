package com.movie.app.modules

open class MovieSearchFilter(
    open var pageNumber: Int = First_PAGE,
    open var sortBy: MovieSortType = MovieSortType.POPULARITY
) {
    companion object {
        const val First_PAGE: Int = 1
    }

    fun isFirstPage(): Boolean = pageNumber == First_PAGE

    fun incrementPage() {
        ++pageNumber
    }
}

enum class MovieSortType(private val displayName: String, val apiSearchName: String) {
    POPULARITY("Popularity", "popularity.desc"),
    RELEASE_DATE("Release Date", "release_date.desc");

    override fun toString(): String {
        return displayName
    }
}
