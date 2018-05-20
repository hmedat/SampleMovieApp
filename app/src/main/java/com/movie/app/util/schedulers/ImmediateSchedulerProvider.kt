package com.movie.app.util.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Provides various threading schedulers.
 */

class ImmediateSchedulerProvider : BaseSchedulerProvider {
    /**
     * IO thread pool scheduler
     */
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    /**
     * Computation thread pool scheduler
     */
    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    /**
     * Main Thread scheduler
     */
    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}
