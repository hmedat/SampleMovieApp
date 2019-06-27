package com.movie.app.util.schedulers

import kotlinx.coroutines.CoroutineDispatcher

interface BaseDispatcher {

    fun computation(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}
