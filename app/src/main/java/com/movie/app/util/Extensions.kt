package com.movie.app.util

import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageView

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
