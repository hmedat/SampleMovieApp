package com.movie.app.util.schedulers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Provides various threading schedulers.
 */
class MainExecutor : BaseExecutor {
    /**
     * IO thread pool scheduler
     */
    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    /**
     * Computation thread pool scheduler
     */
    override fun computation(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    /**
     * Main Thread scheduler
     */
    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}
