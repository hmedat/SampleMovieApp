package com.movie.app.fav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.movie.app.BaseActivity
import com.movie.app.R
import com.movie.app.details.DetailsMovieActivity
import com.movie.app.main.MovieAdapter
import com.movie.app.modules.Movie
import com.movie.app.util.enableToolbarBack
import com.movie.app.util.setDefaultColor
import com.movie.app.util.setToolbar
import kotlinx.android.synthetic.main.activity_favourites.*
import org.koin.android.ext.android.inject
import javax.inject.Inject

class FavouritesActivity : BaseActivity(), FavouritesActivityContractor.View {

    val presenter: FavouritesActivityContractor.Presenter  by inject()
    private lateinit var adapter: MovieAdapter

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, FavouritesActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        setToolbar(favToolbar, R.string.fav_movie_title)
        enableToolbarBack()
        initRecyclerView()
        initRefreshLayout()
        emptyView.error().setOnClickListener { presenter.subscribe() }
        presenter.bindView(this)
        presenter.subscribe()
    }

    private fun initRefreshLayout() {
        swipeLayoutMovies.setDefaultColor()
        swipeLayoutMovies.setOnRefreshListener {
            presenter.subscribe()
        }
    }

    private fun initRecyclerView() {
        adapter = MovieAdapter().apply {
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            setOnItemClickListener { _, _, position ->
                DetailsMovieActivity.startActivity(context, adapter.data[position].id)
            }
            setOnItemChildClickListener { _, _, position ->
                presenter.removeFromList(adapter.data[position].id)
                adapter.remove(position)
                if (adapter.itemCount == 0) {
                    showNoData()
                }
            }
        }
        rvMovies.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        rvMovies.adapter = adapter
    }

    override fun showProgressBar() {
        emptyView.showLoading()
    }

    override fun hideProgressBar() {
        swipeLayoutMovies.isRefreshing = false
    }

    override fun showNoData() {
        emptyView.showEmpty()
    }

    override fun showData(movies: List<Movie>) {
        emptyView.showContent()
        adapter.setNewData(movies)
    }

    override fun showError(throwable: Throwable) {
        emptyView.showError()
    }
}
