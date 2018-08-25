package com.movie.app.repositories.local

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
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

    fun insertMovies(movies: List<Movie>) {
        val favMovieIds = database.movieDao().getFavMovieIds().toHashSet()
        movies.forEach {
            it.isFav = favMovieIds.contains(it.id)
            it.releaseDateLong = DateUtil.parseMovieReleaseDate(it.releaseDate)
        }
        database.movieDao().insert(movies)
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
        return Observable.fromCallable {
            MoviesResult().apply {
                results = when (filter.sortBy) {
                    POPULARITY -> database.movieDao().getMoviesOrderByPopularity(LIMIT_COUNT)
                    RELEASE_DATE -> database.movieDao().getMoviesOrderByReleaseDate(LIMIT_COUNT)
                }
                page = MovieSearchFilter.First_PAGE
                totalPages = MovieSearchFilter.First_PAGE
            }
        }.onErrorReturn {
            val moviesResult = MoviesResult()
            moviesResult
        }.doOnNext {
            Timber.i("Dispatching ${it.results?.size} users from DB...")
        }
    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return Observable.fromCallable {
            var movie = database.movieDao().getMovie(movieId)
            if (movie == null) {
                movie = Movie(Movie.ID_NOT_SET)
            }
            movie
        }.onErrorReturn {
            Movie(Movie.ID_NOT_SET)
        }.filter {
            it.id != Movie.ID_NOT_SET
        }.map {
            it.genres = database.movieGenreDao().getGenresForMovie(it.id)
            it.videosList = database.videoDao().getVideosForMovies(it.id)
            it
        }
    }

    override fun removeAddFavMovie(movieId: Long, isFav: Boolean): Observable<Boolean> {
        return Observable.fromCallable {
            database.movieDao().updateFavMovie(movieId, isFav)
            true
        }
    }

    override fun getFavMovies(): Observable<MoviesResult> {
        return Observable.fromCallable {
            MoviesResult().apply {
                results = database.movieDao().getFavMovies()
            }
        }.doOnNext {
            Timber.d("Dispatching ${it.results?.size} users from DB...")
        }
    }

    override fun getFavMovieIds(): Observable<HashSet<Long>> {
        return Observable.fromCallable {
            database.movieDao().getFavMovieIds().toHashSet()
        }
    }
}
