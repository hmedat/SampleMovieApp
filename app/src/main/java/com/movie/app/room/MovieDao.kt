package com.movie.app.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.movie.app.modules.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY id desc limit 20")
    fun getMovies(): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Long): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: Movie)

    @Delete
    fun delete(vararg movie: Movie)
}
