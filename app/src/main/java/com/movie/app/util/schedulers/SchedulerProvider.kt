package com.movie.app.util.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Provides various threading schedulers.
 */
class SchedulerProvider : BaseSchedulerProvider {
    /**
     * IO thread pool scheduler
     */
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    /**
     * Computation thread pool scheduler
     */
    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    /**
     * Main Thread scheduler
     */
    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
