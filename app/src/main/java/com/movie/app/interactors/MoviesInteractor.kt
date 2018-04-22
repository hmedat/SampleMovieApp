package com.movie.app.interactors

import com.movie.app.api.ApiInterface
import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.Observable
import javax.inject.Inject

class MoviesInteractor @Inject constructor(private val apiInterface: ApiInterface)
    : IMoviesInteractor {


    override fun getLatest(filter: MovieSearchFilter): Observable<LatestMoviesResult> {
        return apiInterface.getLatestMovies(filter.pageNumber)
                .map {
                    MovieMapper.map(it.results!!)
                    it
                }

    }

    override fun findMovie(movieId: Long): Observable<Movie> {
        return apiInterface.findMovie(movieId)
                .map {
                    MovieMapper.map(it)
                    it
                }
    }


}

