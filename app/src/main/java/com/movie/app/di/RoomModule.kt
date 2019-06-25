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
import org.koin.dsl.module
import javax.inject.Singleton


val roomModule = module {
    single { provideDatabase(get()) }
    single { provideLocalMovieRepository(get()) }
    single { provideRemoteMovieRepository(get()) }
    single { provideMovieRepository(get(), get()) }
}

fun provideDatabase(application: MyApp): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "movie-database")
        // allow queries on the main thread.
        // Don't do this on a real app! See PersistenceBasicSample for an example.
        .allowMainThreadQueries()
        .build()
}

fun provideLocalMovieRepository(database: AppDatabase): LocalMovieRepository {
    return LocalMovieRepository(database)
}

fun provideRemoteMovieRepository(apiInterface: ApiInterface): RemoteMovieRepository {
    return RemoteMovieRepository(apiInterface)
}

fun provideMovieRepository(local: LocalMovieRepository, remote: RemoteMovieRepository):
    MovieDataSource {
    return MovieRepository(local, remote)
}
