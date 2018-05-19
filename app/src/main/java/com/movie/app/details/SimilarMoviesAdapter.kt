package com.movie.app.details

import android.graphics.Color
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.movie.app.R
import com.movie.app.modules.Movie
import com.movie.app.util.loadImage

class SimilarMoviesAdapter(data: List<Movie>?) : BaseQuickAdapter<Movie, BaseViewHolder>(R.layout.row_similar_movie, data) {

    override fun convert(helper: BaseViewHolder, item: Movie) {
        helper.setText(R.id.tvMovieTitle, item.title)
        val imageView = helper.getView<ImageView>(R.id.imgPoster)
        imageView.loadImage(item.posterPath, Color.BLACK)
    }
}