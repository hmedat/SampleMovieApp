package com.movie.app.repositories

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.repositories.local.LocalMovieRepository
import com.movie.app.repositories.remote.RemoteMovieRepository
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val local: LocalMovieRepository,
    private var remote: RemoteMovieRepository
) : MovieDataSource {

    override fun getMovies(filter: MovieSearchFilter): Observable<MoviesResult> {
        if (filter.isLoadMore()) {
            return getAndSaveRemoteMovies(filter)
        }
        return Observable.concatArray(
            local.getMovies(filter)
                .onErrorReturn {
                    val moviesResult = MoviesResult()
                    moviesResult
                }, getAndSaveRemoteMovies(filter)
        )
    }

    private fun getAndSaveRemoteMovies(searchFilter: MovieSearchFilter):
            Observable<MoviesResult> {
        return remote.getMovies(searchFilter)
            .doOnNext { it ->
                it.results?.let {
                    local.insertMovies(it)
                    Timber.d("Dispatching ${it.size} users from API...")
                }
            }
    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return Observable.concatArray(local.getMovie(movieId)
            , remote.getMovie(movieId)
                .doOnNext {
                    local.updateMovieDetails(it)
                }
        )
    }

    override fun getFavMovies(): Observable<MoviesResult> {
        return local.getFavMovies()
    }

    override fun removeAddFavMovie(movieId: Long, isFav: Boolean): Observable<Int> {
        return local.removeAddFavMovie(movieId, isFav)
    }

    override fun getFavMovieIds(): Observable<HashSet<Long>> {
        return local.getFavMovieIds()
    }
}
