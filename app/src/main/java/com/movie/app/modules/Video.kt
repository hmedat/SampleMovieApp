package com.movie.app.modules

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(
    tableName = "video", foreignKeys = [(ForeignKey(
        entity = Movie::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("movie_id"),
        onDelete = ForeignKey.CASCADE
    ))]
)
class Video(
    @NonNull @PrimaryKey() var id: String,
    @ColumnInfo(name = "key") var key: String? = null,
    @ColumnInfo(name = "size") var size: Int = 0,
    @ColumnInfo(name = "type") var type: String? = null,
    @ColumnInfo(name = "video_path") var videoPath: String? = null,
    @ColumnInfo(name = "thumb_video_path") var thumbVideoPath: String? = null,
    @ColumnInfo(name = "movie_id") var movieId: Long = 0
)