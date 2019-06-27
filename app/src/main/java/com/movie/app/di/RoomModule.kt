package com.movie.app.di

import android.content.Context
import androidx.room.Room
import com.movie.app.api.ApiInterface
import com.movie.app.repositories.MovieRepository
import com.movie.app.repositories.LocalMovieDataSource
import com.movie.app.repositories.RemoteMovieDataSource
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

fun provideLocalMovieRepository(database: AppDatabase): LocalMovieDataSource {
    return LocalMovieDataSource(database)
}

fun provideRemoteMovieRepository(apiInterface: ApiInterface): RemoteMovieDataSource {
    return RemoteMovieDataSource(apiInterface)
}

fun provideMovieRepository(local: LocalMovieDataSource, remote: RemoteMovieDataSource):
    MovieRepository {
    return MovieRepository(local, remote)
}
