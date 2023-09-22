package com.movie.app.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.annotation.VisibleForTesting
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
