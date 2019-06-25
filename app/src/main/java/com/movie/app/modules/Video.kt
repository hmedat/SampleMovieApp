package com.movie.app.modules

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.annotation.NonNull

@Entity(
    tableName = "video", foreignKeys = [(ForeignKey(
        entity = Movie::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("movie_id"),
        onDelete = ForeignKey.CASCADE
    ))], indices = [Index("movie_id")]
)
data class Video(
    @NonNull @PrimaryKey() var id: String,
    @ColumnInfo(name = "key") var key: String? = null,
    @ColumnInfo(name = "size") var size: Int = 0,
    @ColumnInfo(name = "type") var type: String? = null,
    @ColumnInfo(name = "movie_id") var movieId: Long = 0
)
