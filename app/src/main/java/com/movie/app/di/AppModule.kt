package com.movie.app.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.movie.app.api.ApiInterface
import com.movie.app.api.RequestInterceptor
import com.movie.app.util.schedulers.BaseExecutor
import com.movie.app.util.schedulers.MainExecutor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit

val appModule = module {
    single { provideApiService(get(), get()) }
    single { provideGson() }
    single { provideOkHttpClient(androidContext()) }
    single { provideRxSchedulers() }
}

fun provideApiService(gson: Gson, okHttpClient: OkHttpClient): ApiInterface {
    return Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build().create(ApiInterface::class.java)
}

fun provideGson(): Gson {
    return GsonBuilder().create()
}

const val CACHE_SIZE: Long = 10 * 1024 * 1024
const val CONNECT_TIMEOUT: Long = 30
const val READ_TIMEOUT: Long = 60

fun provideOkHttpClient(context: Context): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val cacheDir = File(context.cacheDir, UUID.randomUUID().toString())
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

fun provideRxSchedulers(): BaseExecutor {
    return MainExecutor()
}
