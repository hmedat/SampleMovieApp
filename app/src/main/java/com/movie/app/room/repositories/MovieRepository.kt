package com.movie.app.room.repositories

import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import com.movie.app.modules.Video
import com.movie.app.room.AppDatabase
import com.movie.app.room.entities.MovieGenreJoin

class MovieRepository constructor(private val database: AppDatabase) : IMovieRepository {


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
            movie.videos?.results?.let {
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

    override fun getMovies() {
    }

    override fun getMovie(movieId: Long) {
    }

}