package com.movie.app.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.movie.app.modules.Movie
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface MovieDao {

    @Query("select * from movie ORDER BY popularity desc limit :limitNumber")
    fun getMoviesOrderByPopularity(limitNumber: Int): Flowable<List<Movie>>

    @Query("select * from movie ORDER BY release_date_long desc limit :limitNumber")
    fun getMoviesOrderByReleaseDate(limitNumber: Int): Flowable<List<Movie>>

    @Query("SELECT * FROM movie WHERE is_Fav = 1")
    fun getFavMovies(): Flowable<List<Movie>>

    @Query("SELECT id FROM movie WHERE is_Fav = 1")
    fun getFavMovieIds(): List<Long>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Long): Maybe<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Movie>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: Movie): List<Long>

    @Delete
    fun delete(vararg movie: Movie)

    @Query("UPDATE movie SET is_Fav = :isFav WHERE id = :id ")
    fun updateFavMovie(id: Long, isFav: Boolean): Int
}
