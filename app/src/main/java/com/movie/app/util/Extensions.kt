package com.movie.app.util

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView

fun ImageView.loadImage(url: String?, colorPlaceholder: Int) {
    GlideApp.with(context)
            .load(url)
            .placeholder(ColorDrawable(colorPlaceholder))
            .error(ColorDrawable(colorPlaceholder))
            .into(this)
}