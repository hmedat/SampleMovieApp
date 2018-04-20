package com.movie.app.details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.movie.app.BaseActivity
import com.movie.app.GlideApp
import com.movie.app.R
import com.movie.app.modules.Movie
import kotlinx.android.synthetic.main.activity_details_movie.*
import javax.inject.Inject

class DetailsMovieActivity : BaseActivity(), DetailsActivityContractor.View {

    @Inject
    lateinit var presenter: DetailsActivityContractor.Presenter

    companion object {
        const val EXTRA_MOVIE: String = "Extra.movie"
        fun startActivity(context: Context, movie: Movie) {
            val intent = Intent(context, DetailsMovieActivity::class.java)
            intent.putExtra(EXTRA_MOVIE, movie)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_movie)
        presenter.bindBundles(intent.extras)
        presenter.subscribe()
    }

    override fun showProgressBar() {
        progressView.showLoading()
    }

    override fun hideProgressBar() {
        progressView.showContent()
    }

    override fun showData(movie: Movie) {
        tvMovieTitle.text = movie.title
        tvReleaseYear.text = movie.releaseDate
        tvRate.text = movie.voteAverage.toString()
        tvGenres.text = movie.genresString
        tvOverView.text = movie.overview

        GlideApp.with(this)
                .load(movie.posterPath)
                .placeholder(ColorDrawable(Color.BLACK))
                .error(ColorDrawable(Color.GRAY))
                .into(imgPoster)
    }

    override fun showError(throwable: Throwable) {
        progressView.showError(R.drawable.ic_no_connection_24dp_white
                , getString(R.string.title_no_connection)
                , getString(R.string.desc_no_connection)
                , getString(R.string.btn_no_connection)) { presenter.subscribe() }
    }

}
