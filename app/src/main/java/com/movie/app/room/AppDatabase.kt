package com.movie.app.room

import androidx.room.Database
import androidx.room.RoomDatabase

import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import com.movie.app.modules.Video
import com.movie.app.room.entities.MovieGenreJoin

@Database(
    entities = [(Movie::class), (MovieGenreJoin::class), (Video::class), (Genre::class)],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun genreDao(): GenreDao

    abstract fun movieGenreDao(): MovieGenreDao

    abstract fun videoDao(): VideoDao
}
