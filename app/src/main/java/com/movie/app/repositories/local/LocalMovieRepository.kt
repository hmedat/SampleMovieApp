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

class LocalMovieRepository(private val database: AppDatabase) : MovieDataSource {

    override fun insertMovies(movies: List<Movie>) {
        val movieGenreJoinList = ArrayList<MovieGenreJoin>()
        val favMovieIds = database.movieDao().getFavMovieIds().toHashSet()
        val genreList = ArrayList<Genre>()
        val videoList = ArrayList<Video>()
        for (movie in movies) {
            movie.genres?.let {
                genreList.addAll(it)
                for (genre in it) {
                    movieGenreJoinList.add(MovieGenreJoin(movie.id, genre.id))
                }
            }
            movie.videoResult?.videos?.let {
                for (video in it) {
                    video.movieId = movie.id
                    videoList.add(video)
                }
            }
            if (favMovieIds.contains(movie.id)) {
                movie.isFav = true
            }
            movie.releaseDate?.let {
                movie.releaseDateLong = DateUtil.parseMovieReleaseDate(it)
            }
        }
        database.movieDao().insert(movies)
        database.genreDao().insert(genreList)
        database.videoDao().insert(videoList)
        database.movieGenreDao().insert(movieGenreJoinList)
    }

    override suspend fun getMovies(searchFilter: MovieSearchFilter): MoviesResult? {
        val result = MoviesResult()
        val limit = 20
        val movies = when (searchFilter.sortBy) {
            MovieSortType.POPULARITY -> database.movieDao().getMoviesOrderByPopularity(limit)
            MovieSortType.RELEASE_DATE -> database.movieDao().getMoviesOrderByReleaseDate(limit)
        }
        Timber.i("Movies ${movies.size} users from DB...")
        for (movie in movies) {
            movie.genres = database.movieGenreDao().getGenresForMovie(movieId = movie.id)
            movie.videosList = database.videoDao().getVideosForMovies(movieId = movie.id)
        }
        result.results = movies
        result.page = MovieSearchFilter.First_PAGE
        result.totalPages = MovieSearchFilter.First_PAGE
        return result
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

    override fun removeAddFavMovie(movieId: Long, isFav: Boolean): Boolean {
        database.movieDao().updateFavMovie(movieId, isFav)
        return true
    }

    override suspend fun getFavMovies(): MoviesResult {
        val result = MoviesResult()
        val movies = database.movieDao().getFavMovies()
        for (movie in movies) {
            movie.genres = database.movieGenreDao().getGenresForMovie(movieId = movie.id)
            movie.videosList = database.videoDao().getVideosForMovies(movieId = movie.id)
        }
        result.results = movies
        return result
    }

    override fun getFavMovieIds(): Observable<HashSet<Long>> {
        return Observable.fromCallable {
            database.movieDao().getFavMovieIds().toHashSet()
        }
    }
}
