package com.movie.app.repositories.remote

import com.movie.app.api.ApiInterface
import com.movie.app.api.result.MoviesResult
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.repositories.MovieDataSource
import io.reactivex.Observable
import javax.inject.Inject

class RemoteMovieRepository @Inject constructor(private val apiInterface: ApiInterface) :
    MovieDataSource {

    override fun insertMovies(movies: List<Movie>) {
    }

    override fun getMovies(searchFilter: MovieSearchFilter): Observable<MoviesResult> {
        return apiInterface.getLatestMovies(
            searchFilter.pageNumber
            , searchFilter.sortBy.apiSearchName
        ).map {
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

    override fun removeAddFavMovie(movieId: Long, isFav: Boolean): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFavMovies(): Observable<MoviesResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFavMovieIds(): Observable<HashSet<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
