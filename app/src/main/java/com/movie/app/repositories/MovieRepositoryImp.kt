package com.movie.app.repositories

import com.movie.app.modules.Movie

import io.reactivex.Observable
import timber.log.Timber

class MovieRepositoryImp(private val local: LocalMovieRepository
                         , private var remote: RemoteMovieRepository) : MovieRepository {

    override fun getMovies(): Observable<List<Movie>> {
        return Observable.concatArray(local.getMovies(),
                remote.getMovies().doOnNext {
                    Timber.d("Dispatching ${it.size} users from API...")
                    local.insertMovies(it)
                })
    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return Observable.concatArray(local.getMovie(movieId), remote.getMovie(movieId))
    }
}
