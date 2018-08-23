package com.movie.app.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import io.reactivex.Observable
import java.io.IOException
import java.util.concurrent.TimeUnit

fun ImageView.loadImage(url: String?, colorPlaceholder: Int) {
    GlideApp.with(context)
        .load(url)
        .placeholder(ColorDrawable(colorPlaceholder))
        .error(ColorDrawable(colorPlaceholder))
        .centerCrop()
        .into(this)
}

fun AppCompatActivity.setToolbar(toolbar: Toolbar) {
    setToolbar(toolbar, "")
}

fun AppCompatActivity.setToolbar(toolbar: Toolbar, resTitle: Int) {
    setToolbar(toolbar, toolbar.context.getString(resTitle))
}

fun AppCompatActivity.setToolbar(toolbar: Toolbar, title: String) {
    setSupportActionBar(toolbar)
    supportActionBar!!.title = title
}

fun AppCompatActivity.setToolbarTitle(title: String?) {
    supportActionBar!!.title = title
}

fun AppCompatActivity.enableToolbarBack() {
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeButtonEnabled(true)
}

fun SwipeRefreshLayout.setDefaultColor() {
    setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE)
}

fun RecyclerView.notifyVisibleItems() {
    val layoutManager = layoutManager as LinearLayoutManager
    if (adapter == null) {
        return
    }
    val firstPos = layoutManager.findFirstVisibleItemPosition()
    val lastPos = layoutManager.findLastVisibleItemPosition()
    for (i in firstPos..lastPos) {
        adapter.notifyItemChanged(i)
    }
}

fun <T> Observable<T>.retryWhenBackoff(
    initialDelay: Long, numRetries: Int, unit: TimeUnit
): Observable<T> {
    return retryWhen(
        RXJavaUtil.exponentialBackoffForExceptions(
            initialDelay, numRetries, unit, IOException::class.java
        )
    )
}

fun <T> Observable<T>.retryWhenBackoffDefault(): Observable<T> {
    return retryWhenBackoff(200, 5, TimeUnit.MILLISECONDS)
}


