package com.movie.app.util

sealed class Result<out T> {
    data class Loading<T>(var loading: Boolean) : Result<T>()
    data class Success<T>(var data: T) : Result<T>()
    data class Failure<T>(val e: Throwable) : Result<T>()

    companion object {
        fun <T> loading(): Result<T> = Loading(true)

        fun <T> success(data: T): Result<T> = Success(data)

        fun <T> failure(e: Throwable): Result<T> = Failure(e)
    }
}
