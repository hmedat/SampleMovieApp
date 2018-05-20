package com.movie.app.api.result

import android.support.annotation.VisibleForTesting
import com.google.gson.annotations.SerializedName
import com.movie.app.modules.Video

class VideoResult @VisibleForTesting constructor(vararg videos: Video) {
    @SerializedName("results")
    var videos: ArrayList<Video>? = null

    init {
        this.videos = ArrayList()
        for (video in videos) {
            this.videos?.add(video)
        }
    }
}
