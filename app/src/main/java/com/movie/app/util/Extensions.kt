package com.movie.app.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

fun androidx.swiperefreshlayout.widget.SwipeRefreshLayout.setDefaultColor() {
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
        adapter?.notifyItemChanged(i)
    }
}

