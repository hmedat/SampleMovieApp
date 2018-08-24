package com.movie.app.modules

open class MovieSearchFilter(
    open var pageNumber: Int = First_PAGE,
    open var sortBy: MovieSortType = MovieSortType.POPULARITY
) {
    companion object {
        const val First_PAGE: Int = 1
    }

    fun isFirstPage(): Boolean = pageNumber == First_PAGE
}
