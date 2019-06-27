package com.movie.app.util.schedulers

import kotlinx.coroutines.CoroutineDispatcher

interface BaseExecutor {

    fun computation(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}
