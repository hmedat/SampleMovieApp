package com.movie.app.main


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.movie.app.GlideApp
import com.movie.app.R
import com.movie.app.modules.Movie


class MovieAdapter : BaseQuickAdapter<Movie, BaseViewHolder>(R.layout.row_movie, null) {

    override fun convert(helper: BaseViewHolder, item: Movie) {
        helper.setText(R.id.tvMovieTitle, item.title)
        helper.setText(R.id.tvReleaseYear, item.releaseDate)
        helper.setText(R.id.tvRate, item.voteAverage.toString())
        helper.setText(R.id.tvOverView, item.overview)
        val imageView = helper.getView<ImageView>(R.id.imgPoster)
        GlideApp.with(imageView.context)
                .load(item.posterPath)
                .placeholder(ColorDrawable(Color.BLACK))
                .error(ColorDrawable(Color.GRAY))
                .into(imageView)
    }

}
