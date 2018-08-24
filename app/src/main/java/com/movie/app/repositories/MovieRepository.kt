package com.movie.app.repositories

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val local: MovieDataSource,
    private var remote: MovieDataSource
) : MovieDataSource {

    override fun insertMovies(movies: List<Movie>) {
        local.insertMovies(movies)
    }

    override fun getMovies(searchFilter: MovieSearchFilter): Observable<MoviesResult> {
        if (searchFilter.pageNumber > 1) {
            return getAndSaveRemoteMovies(searchFilter)
        }
        return Observable.concatArray(local.getMovies(searchFilter)
            .onErrorReturn {
                val moviesResult = MoviesResult()
                moviesResult
            },
            getAndSaveRemoteMovies(searchFilter)
        )
    }

    private fun getAndSaveRemoteMovies(searchFilter: MovieSearchFilter):
            Observable<MoviesResult> {
        return remote.getMovies(searchFilter)
            .doOnNext {
                it.results?.let {
                    insertMovies(it)
                    Timber.d("Dispatching ${it.size} users from API...")
                }
            }
    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return Observable.concatArray(local.getMovie(movieId)
            .onErrorReturn {
                Movie(Movie.ID_NOT_SET)
            }.filter { it.id != Movie.ID_NOT_SET },
            remote.getMovie(movieId)
        )
    }

    override fun getFavMovies(): Observable<MoviesResult> {
        return local.getFavMovies()
    }

    override fun removeAddFavMovie(movieId: Long, isFav: Boolean): Observable<Boolean> {
        return local.removeAddFavMovie(movieId, isFav)
    }

    override fun getFavMovieIds(): Observable<HashSet<Long>> {
        return local.getFavMovieIds()
    }
}
