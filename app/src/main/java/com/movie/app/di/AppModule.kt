package com.movie.app.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.movie.app.MyApp
import com.movie.app.api.ApiInterface
import com.movie.app.api.RequestInterceptor
import com.movie.app.util.schedulers.BaseSchedulerProvider
import com.movie.app.util.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {
    companion object {
        const val CACHE_SIZE: Long = 10 * 1024 * 1024
        const val CONNECT_TIMEOUT: Long = 30
        const val READ_TIMEOUT: Long = 60
    }

    @Provides
    @Singleton
    fun provideApiService(gson: Gson, okHttpClient: OkHttpClient): ApiInterface {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build().create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(application: MyApp): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val cacheDir = File(application.cacheDir, UUID.randomUUID().toString())
        val cache = Cache(cacheDir, CACHE_SIZE)

        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(RequestInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRxSchedulers(): BaseSchedulerProvider {
        return SchedulerProvider()
    }
}
