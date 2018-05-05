package com.movie.app.api.result

import android.support.annotation.VisibleForTesting
import com.movie.app.modules.Video

class VideoResult @VisibleForTesting constructor(vararg videos: Video) {
    var results: ArrayList<Video>? = null

    init {
        results = ArrayList()
        for (video in videos) {
            results?.add(video)
        }
    }

}
