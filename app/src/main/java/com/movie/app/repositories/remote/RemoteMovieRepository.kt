package com.movie.app.repositories.remote

import com.movie.app.api.ApiInterface
import com.movie.app.api.result.MoviesResult
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.repositories.MovieDataSource
import io.reactivex.Observable
import javax.inject.Inject

class RemoteMovieRepository @Inject constructor(private val apiInterface: ApiInterface)
    : MovieDataSource {
    override fun insertMovies(movies: List<Movie>) {
    }

    override fun getMovies(searchFilter: MovieSearchFilter): Observable<MoviesResult> {
        return apiInterface.getLatestMovies(searchFilter.pageNumber)
                .map {
                    MovieMapper.map(it.results!!)
                    it
                }
    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return apiInterface.findMovie(movieId).map {
            MovieMapper.map(it)
            it
        }
    }
}
