package com.movie.app.repositories.local

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.modules.MovieSearchFilter.Companion.First_PAGE
import com.movie.app.modules.MovieSortType.POPULARITY
import com.movie.app.modules.MovieSortType.RELEASE_DATE
import com.movie.app.modules.Video
import com.movie.app.repositories.MovieDataSource
import com.movie.app.room.AppDatabase
import com.movie.app.room.entities.MovieGenreJoin
import com.movie.app.util.DateUtil
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class LocalMovieRepository @Inject constructor(private val database: AppDatabase) :
    MovieDataSource {
    companion object {
        private const val LIMIT_COUNT: Int = 20
    }

    fun insertMovies(movies: List<Movie>): List<Long> {
        val favMovieIds = database.movieDao().getFavMovieIds().toHashSet()
        movies.forEach {
            it.isFav = favMovieIds.contains(it.id)
            it.releaseDateLong = DateUtil.parseMovieReleaseDate(it.releaseDate)
        }
        return database.movieDao().insert(movies)
    }

    fun updateMovieDetails(movie: Movie) {
        val genreJoinList = ArrayList<MovieGenreJoin>()
        val genreList = ArrayList<Genre>()
        val videoList = ArrayList<Video>()
        movie.genres?.forEach {
            genreList.add(it)
            genreJoinList.add(MovieGenreJoin(movie.id, it.id))
        }
        movie.videoResult?.videos?.forEach {
            it.movieId = movie.id
            videoList.add(it)
        }
        database.genreDao().insert(genreList)
        database.videoDao().insert(videoList)
        database.movieGenreDao().insert(genreJoinList)
    }

    override fun getMovies(filter: MovieSearchFilter): Observable<MoviesResult> {
        return when (filter.sortBy) {
            POPULARITY -> database.movieDao().getMoviesOrderByPopularity(LIMIT_COUNT)
            RELEASE_DATE -> database.movieDao().getMoviesOrderByReleaseDate(LIMIT_COUNT)
        }
            .map {
                MoviesResult(results = it, page = First_PAGE, totalPages = First_PAGE)
            }.doOnNext {
                Timber.i("Dispatching ${it.results?.size} users from DB...")
            }.toObservable()
    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return database.movieDao().getMovie(movieId)
            .onErrorReturn {
                Movie(Movie.ID_NOT_SET)
            }.map {
                it.genres = database.movieGenreDao().getGenresForMovie(it.id)
                it.videosList = database.videoDao().getVideosForMovies(it.id)
                it
            }
            .toObservable()
    }

    override fun removeAddFavMovie(movieId: Long, isFav: Boolean): Observable<Int> {
        return Observable.fromCallable { database.movieDao().updateFavMovie(movieId, isFav) }
    }

    override fun getFavMovies(): Observable<MoviesResult> {
        return database.movieDao().getFavMovies()
            .map {
                MoviesResult(results = it, page = First_PAGE, totalPages = First_PAGE)
            }.doOnNext {
                Timber.d("Dispatching ${it.results?.size} users from DB...")
            }.toObservable()
    }

    override fun getFavMovieIds(): Observable<HashSet<Long>> {
        return Observable.fromCallable {
            database.movieDao().getFavMovieIds().toHashSet()
        }
    }
}
