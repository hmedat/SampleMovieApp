package com.movie.app.details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.movie.app.BaseActivity
import com.movie.app.R
import com.movie.app.modules.Movie
import com.movie.app.util.loadImage
import kotlinx.android.synthetic.main.activity_details_movie.*
import javax.inject.Inject

class DetailsMovieActivity : BaseActivity(), DetailsActivityContractor.View {

    @Inject
    lateinit var presenter: DetailsActivityContractor.Presenter

    companion object {
        const val EXTRA_MOVIE: String = "Extra.movie"
        fun startActivity(context: Context, movieId: Long) {
            val intent = Intent(context, DetailsMovieActivity::class.java)
            intent.putExtra(EXTRA_MOVIE, movieId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_movie)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val movieId : Long = intent.extras.getLong(EXTRA_MOVIE)
        presenter.setMovieId(movieId)
        presenter.subscribe()
        imgVideo.setOnClickListener {
            presenter.showTrailerVideo()
        }
        imgIconVideo.setOnClickListener {
            presenter.showTrailerVideo()
        }
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
        imgPoster.loadImage(movie.posterPath, Color.BLACK)
        imgVideo.loadImage(movie.firstVideoImageUrl, Color.BLACK)
        imgIconVideo.visibility = View.VISIBLE
        imgRate.visibility = View.VISIBLE
        supportActionBar?.title = movie.title;
    }

    override fun showError(throwable: Throwable) {
        progressView.showError(R.drawable.ic_no_connection_24dp_white
                , getString(R.string.title_no_connection)
                , getString(R.string.desc_no_connection)
                , getString(R.string.btn_no_connection)) { presenter.subscribe() }
    }

    override fun startYoutubeActivity(chooser: Intent) {
        if (chooser.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        }
    }

}
