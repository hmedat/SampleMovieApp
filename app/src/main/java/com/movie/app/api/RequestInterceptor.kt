package com.movie.app.api

import com.movie.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response



class RequestInterceptor : Interceptor {
    companion object {
        private const val API_KEY = "3d6c79a2c15d8b742f15246311632b08"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.MOVIE_API_KEY)
                .build()

        // Request customization: add request headers
        val requestBuilder = original.newBuilder()
                .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
