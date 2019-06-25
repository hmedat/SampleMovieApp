package com.movie.app.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movie.app.modules.Movie

@Dao
interface MovieDao {

    @Query("select * from movie ORDER BY popularity desc limit :limitNumber")
    fun getMoviesOrderByPopularity(limitNumber: Int): List<Movie>

    @Query("select * from movie ORDER BY release_date_long desc limit :limitNumber")
    fun getMoviesOrderByReleaseDate(limitNumber: Int): List<Movie>

    @Query("SELECT * FROM movie WHERE is_Fav = 1")
    fun getFavMovies(): List<Movie>

    @Query("SELECT id FROM movie WHERE is_Fav = 1")
    fun getFavMovieIds(): List<Long>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Long): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: Movie)

    @Delete
    fun delete(vararg movie: Movie)

    @Query("UPDATE movie SET is_Fav = :isFav WHERE id = :id ")
    fun updateFavMovie(id: Long, isFav: Boolean): Int
}
