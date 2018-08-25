package com.movie.app.repositories.local

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.modules.MovieSortType
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

    fun insertMovies(movies: List<Movie>) {
        val favMovieIds = database.movieDao().getFavMovieIds().toHashSet()
        for (movie in movies) {
            movie.isFav = favMovieIds.contains(movie.id)
            movie.releaseDateLong = DateUtil.parseMovieReleaseDate(movie.releaseDate)
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

    override fun getMovies(searchFilter: MovieSearchFilter): Observable<MoviesResult> {
        return Observable.fromCallable {
            val result = MoviesResult()
            val limit = 20
            val movies = when (searchFilter.sortBy) {
                MovieSortType.POPULARITY -> database.movieDao().getMoviesOrderByPopularity(limit)
                MovieSortType.RELEASE_DATE -> database.movieDao().getMoviesOrderByReleaseDate(limit)
            }
            Timber.i("Movies ${movies.size} users from DB...")
            result.results = movies
            result.page = MovieSearchFilter.First_PAGE
            result.totalPages = MovieSearchFilter.First_PAGE
            result
        }.doOnNext {
            Timber.i("Dispa tching ${it.results?.size} users from DB...")
        }
    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return Observable.fromCallable {
            var movie = database.movieDao().getMovie(movieId)
            if (movie == null) {
                movie = Movie(Movie.ID_NOT_SET)
            } else {
                movie.genres = database.movieGenreDao().getGenresForMovie(movieId = movie.id)
                movie.videosList = database.videoDao().getVideosForMovies(movieId = movie.id)
            }
            movie
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
            val latestMoviesResult = MoviesResult()
            val movies = database.movieDao().getFavMovies()
            for (movie in movies) {
                movie.genres = database.movieGenreDao().getGenresForMovie(movieId = movie.id)
                movie.videosList = database.videoDao().getVideosForMovies(movieId = movie.id)
            }
            latestMoviesResult.results = movies
            latestMoviesResult
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
