package com.movie.app.modules

class MovieSearchFilter {
    companion object {
        const val First_PAGE: Int = 1
    }

    var pageNumber: Int = First_PAGE

    fun isFirstPage(): Boolean = pageNumber == First_PAGE
}
