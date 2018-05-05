package com.movie.app.repositories

import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import com.movie.app.modules.Video
import com.movie.app.room.AppDatabase
import com.movie.app.room.entities.MovieGenreJoin
import io.reactivex.Observable
import timber.log.Timber

class LocalMovieRepositoryImp constructor(private val database: AppDatabase) : LocalMovieRepository {


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

    override fun getMovies(): Observable<List<Movie>> {
        return Observable.fromCallable {
            val movies = database.movieDao().getMovies()
            for (movie in movies) {
                movie.genres = database.movieGenreDao().getGenresForMovie(movieId = movie.id)
                movie.videos = database.videoDao().getVideosForMovies(movieId = movie.id)
            }
            movies
        }.filter { it.isNotEmpty() }
                .doOnNext {
                    Timber.d("Dispatching ${it.size} users from DB...")
                }

    }

    override fun getMovie(movieId: Long): Observable<Movie> {
        return Observable.fromCallable {
            val movie = database.movieDao().getMovie(movieId)
            movie.genres = database.movieGenreDao().getGenresForMovie(movieId = movie.id)
            movie.videos = database.videoDao().getVideosForMovies(movieId = movie.id)
            movie
        }
    }

}