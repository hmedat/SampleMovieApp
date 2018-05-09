package com.movie.app.repositories.local

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.modules.Video
import com.movie.app.repositories.MovieDataSource
import com.movie.app.room.AppDatabase
import com.movie.app.room.entities.MovieGenreJoin
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class LocalMovieRepository @Inject constructor(private val database: AppDatabase)
    : MovieDataSource {


    override fun insertMovies(movies: List<Movie>) {
        val movieGenreJoinList = ArrayList<MovieGenreJoin>()
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
        }
        database.movieDao().insert(movies)
        database.genreDao().insert(genreList)
        database.videoDao().insert(videoList)
        database.movieGenreDao().insert(movieGenreJoinList)
    }

    override fun getMovies(searchFilter: MovieSearchFilter): Observable<MoviesResult> {
        return Observable.fromCallable {
            val latestMoviesResult = MoviesResult()
            val movies = database.movieDao().getMovies()
            for (movie in movies) {
                movie.genres = database.movieGenreDao().getGenresForMovie(movieId = movie.id)
                movie.videosList = database.videoDao().getVideosForMovies(movieId = movie.id)
            }
            latestMoviesResult.results = movies
            latestMoviesResult.page = MovieSearchFilter.First_PAGE
            latestMoviesResult.totalPages = MovieSearchFilter.First_PAGE
            latestMoviesResult
        }.filter { it.results?.isNotEmpty()!! }
                .doOnNext {
                    Timber.d("Dispatching ${it.results?.size} users from DB...")
                }

    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return Observable.fromCallable {
            val movie = database.movieDao().getMovie(movieId)
            movie.genres = database.movieGenreDao().getGenresForMovie(movieId = movie.id)
            movie.videosList = database.videoDao().getVideosForMovies(movieId = movie.id)
            movie
        }
    }

}