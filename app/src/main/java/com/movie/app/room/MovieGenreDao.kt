package com.movie.app.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.support.annotation.VisibleForTesting
import com.movie.app.modules.Genre
import com.movie.app.room.entities.MovieGenreJoin

@Dao
interface MovieGenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieGenreJoin: MovieGenreJoin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieGenreJoin: List<MovieGenreJoin>)

    @VisibleForTesting
    @Query("SELECT * FROM movie_genre_join")
    fun getAll(): List<MovieGenreJoin>

    @Query("SELECT * FROM genre INNER JOIN movie_genre_join ON genre.id=movie_genre_join.genre_id WHERE movie_genre_join.movie_id=:movieId")
    fun getGenresForMovie(movieId: Long): List<Genre>
}
