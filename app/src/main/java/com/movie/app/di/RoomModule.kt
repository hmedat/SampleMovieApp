package com.movie.app.di

import android.arch.persistence.room.Room
import com.movie.app.MyApp
import com.movie.app.room.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(application: MyApp): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "movie-database")
                // allow queries on the main thread.
                // Don't do this on a real app! See PersistenceBasicSample for an example.
                .allowMainThreadQueries()
                .build()
    }
}
