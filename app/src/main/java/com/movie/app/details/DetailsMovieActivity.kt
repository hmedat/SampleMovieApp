package com.movie.app.details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.movie.app.BaseActivity
import com.movie.app.R
import com.movie.app.modules.Movie
import com.movie.app.util.GenreUtil
import com.movie.app.util.enableToolbarBack
import com.movie.app.util.loadImage
import com.movie.app.util.setToolbar
import com.movie.app.util.setToolbarTitle
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_details_movie.*
import org.koin.android.ext.android.inject
import javax.inject.Inject

class DetailsMovieActivity : BaseActivity(), DetailsActivityContractor.View {

    val presenter: DetailsActivityContractor.Presenter  by inject()

    companion object {
        const val EXTRA_MOVIE_ID: String = "Extra.Movie.Id"
        fun startActivity(context: Context, movieId: Long) {
            val intent = Intent(context, DetailsMovieActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_movie)
        val movieId: Long = intent.extras.getLong(EXTRA_MOVIE_ID)
        presenter.setMovieId(movieId)
        setToolbar(detailsToolbar)
        enableToolbarBack()
        emptyViewDetails.error().setOnClickListener { presenter.subscribe() }
        presenter.bindView(this)
        presenter.subscribe()
    }

    override fun showProgressBar() {
        emptyViewDetails.showLoading()
    }

    override fun hideProgressBar() {
        emptyViewDetails.showContent()
    }

    override fun showData(movie: Movie) {
        tvMovieTitle.text = movie.title
        tvReleaseYear.text = movie.releaseDate
        tvRate.text = movie.voteAverage.toString()
        tvGenres.text = GenreUtil.getGenreString(movie.genres)
        tvOverView.text = movie.overview
        imgRate.visibility = View.VISIBLE
        setToolbarTitle(movie.title)
        imgPoster.loadImage(movie.posterPath, Color.BLACK)
        handleVideoData(movie)
    }

    private fun handleVideoData(movie: Movie) {
        if (movie.videosList == null || movie.videosList!!.isEmpty()) {
            return
        }
        youtubePlayerView.visibility = View.VISIBLE
        val video = movie.videosList!![0]
        youtubePlayerView.initialize({ player ->
            player.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    player.cueVideo(video.key, 0f)
                }
            })
        }, true)
    }

    override fun showError(throwable: Throwable) {
        emptyViewDetails.showError()
    }

    override fun showSimilarMovies(list: List<Movie>) {
        val similarAdapter = SimilarMoviesAdapter(list)
        rvSimilarMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarAdapter
            visibility = View.VISIBLE
        }
        similarAdapter.setOnItemClickListener { _, _, position ->
            val movie = similarAdapter.data[position]
            DetailsMovieActivity.startActivity(context, movie.id)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        youtubePlayerView.release()
    }
}
