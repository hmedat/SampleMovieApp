package com.movie.app.repositories.remote

import com.movie.app.api.ApiInterface
import com.movie.app.api.result.MoviesResult
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.repositories.MovieDataSource

class RemoteMovieRepository(private val apiInterface: ApiInterface) : MovieDataSource {

    override fun insertMovies(movies: List<Movie>) {
    }

    override suspend fun getMovies(searchFilter: MovieSearchFilter): MoviesResult? {
        val deferred = apiInterface.getLatestMoviesAsync(searchFilter.pageNumber, searchFilter.sortBy.apiSearchName)
        val result = deferred.await().body()
        MovieMapper.map(result?.results ?: listOf())
        return result
    }

    override suspend fun getMovie(movieId: Long): Movie? {
        return apiInterface.findMovieAsync(movieId).await().body()?.apply {
            MovieMapper.map(this)
        }
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
