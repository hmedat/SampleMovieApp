package com.movie.app.api

import com.movie.app.api.result.LatestMoviesResult

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("discover/movie")
    fun getLatestMovies(@Query("page") pageNumber: Int): Observable<LatestMoviesResult>

}