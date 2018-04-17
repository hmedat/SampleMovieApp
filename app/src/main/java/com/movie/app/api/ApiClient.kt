package com.movie.app.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.movie.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private var instance: ApiInterface? = null
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    fun getInstance(): ApiInterface {
        if (instance == null) {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(RequestInterceptor())
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(interceptor)
            }
            val sRetrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build())
                    .build()
            instance = sRetrofit.create(ApiInterface::class.java)
        }
        return instance!!
    }

}
