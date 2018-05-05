package com.movie.app.room

import android.arch.persistence.room.*
import com.movie.app.modules.Video

@Dao
interface VideoDao {

    @Query("SELECT * FROM video")
    fun getVideos(): List<Video>

    @Query("SELECT * FROM video WHERE id = :id")
    fun getVideo(id: Long): Video

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg video: Video)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(videos: List<Video>)

    @Delete
    fun delete(video: Video)
}
