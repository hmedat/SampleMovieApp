package com.movie.app.fav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.movie.app.BaseActivity
import com.movie.app.R
import com.movie.app.details.DetailsMovieActivity
import com.movie.app.main.MovieAdapter
import com.movie.app.util.ResultState
import com.movie.app.util.enableToolbarBack
import com.movie.app.util.setDefaultColor
import com.movie.app.util.setToolbar
import kotlinx.android.synthetic.main.activity_favourites.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesActivity : BaseActivity() {

    private val viewModel: FavouritesMoviesViewModel  by viewModel()
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
        emptyView.error().setOnClickListener { viewModel.subscribe() }
        viewModel.getResultLiveData().observe(this, Observer {
            when (it.status) {
                ResultState.LOADING -> {
                    emptyView.showLoading()
                }
                ResultState.SUCCESS -> {
                    val list = it.data?.results ?: listOf()
                    swipeLayoutMovies.isRefreshing = false
                    if (list.isEmpty()) {
                        emptyView.showEmpty()
                    } else {
                        emptyView.showContent()
                        adapter.setNewData(list)
                    }
                }
                ResultState.ERROR -> {
                    emptyView.showError()
                    swipeLayoutMovies.isRefreshing = false
                }
            }
        })
        viewModel.subscribe()
    }

    private fun initRefreshLayout() {
        swipeLayoutMovies.setDefaultColor()
        swipeLayoutMovies.setOnRefreshListener {
            viewModel.subscribe()
        }
    }

    private fun initRecyclerView() {
        adapter = MovieAdapter().apply {
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
        }
        adapter.setOnItemClickListener { _, _, position ->
            DetailsMovieActivity.startActivity(context, adapter.data[position].id)
        }
        adapter.setOnItemChildClickListener { _, _, position ->
            viewModel.removeFromList(adapter.data[position].id)
            adapter.remove(position)
            if (adapter.itemCount == 0) {
                this@FavouritesActivity.emptyView.showEmpty()
            }
        }
        rvMovies.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        rvMovies.adapter = adapter
    }

}
