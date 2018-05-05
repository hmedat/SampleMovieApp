package com.movie.app.room.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import com.movie.app.modules.Genre
import com.movie.app.modules.Movie

@Entity(tableName = "movie_genre_join", primaryKeys = ["movie_id", "genre_id"]
        , foreignKeys = [(ForeignKey(entity = Movie::class, parentColumns = arrayOf("id")
        , childColumns = arrayOf("movie_id"))), (ForeignKey(entity = Genre::class
        , parentColumns = arrayOf("id"), childColumns = arrayOf("genre_id")))])
data class MovieGenreJoin(@ColumnInfo(name = "movie_id") var movieId: Long, @ColumnInfo(name = "genre_id") var genreId: Long)