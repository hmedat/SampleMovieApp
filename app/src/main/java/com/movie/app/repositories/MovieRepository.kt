package com.movie.app.repositories

import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.modules.MoviesResult
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

    suspend fun getRemoteMovie(movieId: Long): Movie? {
        return remote.getMovie(movieId)
    }

    suspend fun getLocalMovies(searchFilter: MovieSearchFilter): MoviesResult? {
        return local.getMovies(searchFilter)
    }

    suspend fun getLocalMovie(movieId: Long): Movie? {
        return local.getMovie(movieId)
    }

    suspend fun getFavMovies(): MoviesResult {
        return local.getFavMovies()
    }

    fun removeAddFavMovie(movieId: Long, isFav: Boolean): Boolean {
        return local.removeAddFavMovie(movieId, isFav)
    }

    fun getFavMovieIds(): HashSet<Long> {
        return local.getFavMovieIds()
    }

    suspend fun getSimilarMovies(movieId: Long): MoviesResult? {
        val result = remote.getSimilarMovies(movieId)
        result?.results?.let {
            local.insertMovies(it)
        }
        return result
    }
}
