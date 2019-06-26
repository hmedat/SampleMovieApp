package com.movie.app.util

class LiveDataResult<out T> private constructor(val status: ResultState, val data: T?, val throwable: Throwable?) {

    companion object {
        fun <T> success(data: T?): LiveDataResult<T> {
            return LiveDataResult(ResultState.SUCCESS, data, null)
        }

        fun <T> error(error: Throwable?): LiveDataResult<T> {
            return LiveDataResult(ResultState.ERROR, null, error)
        }

        fun <T> loading(): LiveDataResult<T> {
            return LiveDataResult(ResultState.LOADING, null, null)
        }
    }
}

enum class ResultState {
    LOADING, SUCCESS, ERROR
}


class PaginationLiveDataResult<out T> private constructor(
    val status: PaginationResultState,
    val data: T? = null,
    val throwable: Throwable? = null
) {

    companion object {

        fun <T> firstData(data: T?): PaginationLiveDataResult<T> {
            return PaginationLiveDataResult(PaginationResultState.FIRST_DATA, data)
        }

        fun <T> moreData(data: T?): PaginationLiveDataResult<T> {
            return PaginationLiveDataResult(PaginationResultState.MORE_DATA, data)
        }

        fun <T> error(error: Throwable?): PaginationLiveDataResult<T> {
            return PaginationLiveDataResult(PaginationResultState.ERROR, null, error)
        }

        fun <T> loading(): PaginationLiveDataResult<T> {
            return PaginationLiveDataResult(PaginationResultState.LOADING)
        }

        fun <T> noData(): PaginationLiveDataResult<T> {
            return PaginationLiveDataResult(PaginationResultState.NO_DATA)
        }
    }
}

enum class PaginationResultState {
    LOADING, FIRST_DATA, MORE_DATA, NO_DATA, ERROR
}
