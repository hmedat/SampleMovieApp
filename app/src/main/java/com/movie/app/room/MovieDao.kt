package com.movie.app.room

import android.arch.persistence.room.*
import com.movie.app.modules.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getMovies(): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Long): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: Movie)

    @Delete
    fun delete(vararg movie: Movie)

}
