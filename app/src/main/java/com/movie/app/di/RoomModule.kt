package com.movie.app.di

import android.arch.persistence.room.Room
import com.movie.app.MyApp
import com.movie.app.api.ApiInterface
import com.movie.app.repositories.MovieDataSource
import com.movie.app.repositories.MovieRepository
import com.movie.app.repositories.local.LocalMovieRepository
import com.movie.app.repositories.remote.RemoteMovieRepository
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
                .build()
    }

    @Provides
    @Singleton
    fun provideLocalMovieRepository(database: AppDatabase): LocalMovieRepository {
        return LocalMovieRepository(database)
    }

    @Provides
    @Singleton
    fun provideRemoteMovieRepository(apiInterface: ApiInterface): RemoteMovieRepository {
        return RemoteMovieRepository(apiInterface)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(local: LocalMovieRepository, remote: RemoteMovieRepository):
            MovieDataSource {
        return MovieRepository(local, remote)
    }
}
