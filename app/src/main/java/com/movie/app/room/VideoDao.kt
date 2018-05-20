package com.movie.app.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.support.annotation.VisibleForTesting
import com.movie.app.modules.Video

@Dao
interface VideoDao {

    @VisibleForTesting
    @Query("SELECT * FROM video")
    fun getVideos(): List<Video>

    @Query("SELECT * FROM video WHERE movie_id = :movieId")
    fun getVideosForMovies(movieId: Long): List<Video>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg video: Video)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(videos: List<Video>)

    @Delete
    fun delete(video: Video)
}
