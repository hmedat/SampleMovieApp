package com.movie.app.main

import android.graphics.Color
import android.support.v7.util.DiffUtil
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.movie.app.R
import com.movie.app.modules.Movie
import com.movie.app.util.MovieDiffCallback
import com.movie.app.util.loadImage

class MovieAdapter : BaseQuickAdapter<Movie, BaseViewHolder>(R.layout.row_movie, null) {

    override fun convert(helper: BaseViewHolder, item: Movie) {
        helper.setText(R.id.tvMovieTitle, item.title)
        helper.setText(R.id.tvReleaseYear, item.releaseDate)
        helper.setText(R.id.tvRate, item.voteAverage.toString())
        helper.setText(R.id.tvOverView, item.overview)
        val resFav = if (item.isFav) R.drawable.ic_favorite else R.drawable.ic_unfavorite
        helper.setImageResource(R.id.imgFav, resFav)
        val imageView = helper.getView<ImageView>(R.id.imgPoster)
        imageView.loadImage(item.posterPath, Color.BLACK)
        helper.addOnClickListener(R.id.imgFav)
    }

    override fun setNewData(newData: List<Movie>?) {
        if (newData == null) {
            this.data.clear()
            notifyDataSetChanged()
            return
        }
        val diffCallback = MovieDiffCallback(mData, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.data.clear()
        this.data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}
