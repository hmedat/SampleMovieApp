package com.movies.catalog

import org.mockito.Mockito

object TestUtil {
    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}
