package com.movie.app.repositories

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.Observable
import timber.log.Timber

class MovieRepository(private val local: MovieDataSource, private var remote: MovieDataSource) {

    suspend fun getRemoteMovies(searchFilter: MovieSearchFilter): MoviesResult? {
        val result = remote.getMovies(searchFilter)
        result?.results?.let {
            local.insertMovies(it)
            Timber.d("Dispatching ${it.size} users from API...")
        }
        return result
    }

    suspend fun getLocalMovies(searchFilter: MovieSearchFilter): MoviesResult? {
        return local.getMovies(searchFilter)
    }

    fun getMovie(movieId: Long): Observable<Movie> {
        return Observable.concatArray(local.getMovie(movieId)
            .onErrorReturn {
                Movie(Movie.ID_NOT_SET)
            }.filter { it.id != Movie.ID_NOT_SET },
            remote.getMovie(movieId)
        )
    }

    suspend fun getFavMovies(): MoviesResult {
        return local.getFavMovies()
    }

    fun removeAddFavMovie(movieId: Long, isFav: Boolean): Boolean {
        return local.removeAddFavMovie(movieId, isFav)
    }

    fun getFavMovieIds(): Observable<HashSet<Long>> {
        return local.getFavMovieIds()
    }
}
