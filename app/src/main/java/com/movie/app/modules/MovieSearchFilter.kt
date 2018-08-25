package com.movie.app.modules

data class MovieSearchFilter(
    var pageNumber: Int = First_PAGE,
    var sortBy: MovieSortType = MovieSortType.POPULARITY
) {
    companion object {
        const val First_PAGE: Int = 1
    }

    fun isFirstPage(): Boolean = pageNumber == First_PAGE

    fun isLoadMore(): Boolean = pageNumber > First_PAGE
}
