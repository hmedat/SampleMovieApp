package com.movie.app.repositories

import com.movie.app.api.ApiInterface
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import io.reactivex.Observable

class RemoteMovieRepositoryImp constructor(private val apiInterface: ApiInterface)
    : RemoteMovieRepository {

    override fun getMovies(): Observable<List<Movie>> {
        return apiInterface.getLatestMovies(0)
                .map {
                    MovieMapper.map(it.results!!)
                    it.results
                }
    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return apiInterface.findMovie(movieId).map {
            MovieMapper.map(it)
            it
        }
    }

}