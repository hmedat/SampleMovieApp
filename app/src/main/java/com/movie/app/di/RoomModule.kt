package com.movie.app.di

import android.content.Context
import androidx.room.Room
import com.movie.app.api.ApiInterface
import com.movie.app.repositories.MovieDataSource
import com.movie.app.repositories.MovieRepository
import com.movie.app.repositories.local.LocalMovieRepository
import com.movie.app.repositories.remote.RemoteMovieRepository
import com.movie.app.room.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val roomModule = module {
    single { provideDatabase(androidContext()) }
    single { provideLocalMovieRepository(get()) }
    single { provideRemoteMovieRepository(get()) }
    single { provideMovieRepository(get(), get()) }
}

fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "movie-database")
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
