package com.movie.app.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import com.movie.app.modules.Video
import com.movie.app.room.entities.MovieGenreJoin

@Database(entities = [(Movie::class), (MovieGenreJoin::class), (Video::class), (Genre::class)]
        , version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun genreDao(): GenreDao

    abstract fun movieGenreDao(): MovieGenreDao

    abstract fun videoDao(): VideoDao

}
