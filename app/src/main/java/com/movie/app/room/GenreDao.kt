package com.movie.app.room

import android.arch.persistence.room.*
import com.movie.app.modules.Genre

@Dao
interface GenreDao {

    @Query("SELECT * FROM genre")
    fun getGenres(): List<Genre>

    @Query("SELECT * FROM genre WHERE id = :id")
    fun getGenre(id: Long): Genre

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg genre: Genre)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: List<Genre>)

    @Delete
    fun delete(genre: Genre)
}
