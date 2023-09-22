package com.movie.app.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.movie.app.modules.Genre
import com.movie.app.modules.Movie

@Entity(
    tableName = "movie_genre_join", primaryKeys = ["movie_id", "genre_id"],
    foreignKeys = [(ForeignKey(
        entity = Movie::class, parentColumns = arrayOf("id"),
        childColumns = arrayOf("movie_id")
    )), (ForeignKey(
        entity = Genre::class,
        parentColumns = arrayOf("id"), childColumns = arrayOf("genre_id")
    ))],
    indices = [Index("genre_id")]
)
data class MovieGenreJoin(
    @ColumnInfo(name = "movie_id") var movieId: Long,
    @ColumnInfo(name = "genre_id") var genreId: Long
)
