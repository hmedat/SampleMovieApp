package com.movie.app.repositories

import com.movie.app.api.ApiInterface
import com.movie.app.api.result.MoviesResult
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter

class RemoteMovieDataSource(private val apiInterface: ApiInterface) : MovieDataSource {

    override fun insertMovies(movies: List<Movie>) {
    }

    override suspend fun getMovies(filter: MovieSearchFilter): MoviesResult? {
        return apiInterface.getLatestMoviesAsync(filter.pageNumber, filter.sortBy.apiSearchName).apply {
            MovieMapper.map(results ?: listOf())
        }
    }

    override suspend fun getMovie(movieId: Long): Movie? {
        return apiInterface.findMovieAsync(movieId).apply {
            MovieMapper.map(this)
        }
    }

    override suspend fun getSimilarMovies(movieId: Long): MoviesResult? {
        val result = apiInterface.getSimilarMoviesAsync(movieId)
        MovieMapper.map(result.results ?: listOf())
        return result
    }

    override fun removeAddFavMovie(movieId: Long, isFav: Boolean): Boolean {
        TODO("not implemented")
    }

    override suspend fun getFavMovies(): MoviesResult {
        TODO("not implemented")
    }

    override fun getFavMovieIds(): HashSet<Long> {
        TODO("not implemented")
    }
}
