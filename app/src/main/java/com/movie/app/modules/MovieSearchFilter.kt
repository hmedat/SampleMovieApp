package com.movie.app.modules

open class MovieSearchFilter(open var pageNumber: Int = First_PAGE) {
    companion object {
        const val First_PAGE: Int = 1
        const val POPULARITY_DESC: String = "popularity"
    }

    fun isFirstPage(): Boolean = pageNumber == First_PAGE
}
