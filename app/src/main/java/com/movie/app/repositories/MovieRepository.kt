package com.movie.app.repositories

import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.repositories.remote.RemoteMovieRepository

import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class MovieRepository @Inject constructor(private val local: MovieDataSource
                                          , private var remote: RemoteMovieRepository)
    : MovieDataSource {

    override fun insertMovies(movies: List<Movie>) {

    }

    override fun getMovies(searchFilter: MovieSearchFilter): Observable<LatestMoviesResult> {
        if (searchFilter.pageNumber > 1) {
            return getAndSaveRemoteMovies(searchFilter)
        }
        return Observable.concatArray(local.getMovies(searchFilter)
                , getAndSaveRemoteMovies(searchFilter))
    }

    private fun getAndSaveRemoteMovies(searchFilter: MovieSearchFilter)
            : Observable<LatestMoviesResult> {
        return remote.getMovies(searchFilter).doOnNext {
            it.results?.let {
                local.insertMovies(it)
                Timber.d("Dispatching ${it.size} users from API...")
            }
        }
    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return Observable.concatArray(local.getMovie(movieId), remote.getMovie(movieId))
    }
}
