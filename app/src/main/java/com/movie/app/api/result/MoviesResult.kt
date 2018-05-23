package com.movie.app.api.result

import com.google.gson.annotations.SerializedName
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter

class MoviesResult {

    var page: Int = 0
    @SerializedName("total_pages")
    var totalPages: Int = 0
    var results: List<Movie>? = null

    fun isLoadMore(): Boolean = page > MovieSearchFilter.First_PAGE

    fun isFinished(): Boolean = page == totalPages

    fun isEmptyResult(): Boolean = results == null || results!!.isEmpty()
}
