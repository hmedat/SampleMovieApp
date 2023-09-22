package com.movie.app.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.annotation.VisibleForTesting
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

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM genre INNER JOIN movie_genre_join " +
            "ON genre.id=movie_genre_join.genre_id WHERE movie_genre_join.movie_id=:movieId")
    fun getGenresForMovie(movieId: Long): List<Genre>
}
