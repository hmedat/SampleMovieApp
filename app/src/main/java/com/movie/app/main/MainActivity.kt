package com.movie.app.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.movie.app.BaseActivity
import com.movie.app.R
import com.movie.app.details.DetailsMovieActivity
import com.movie.app.modules.MovieSortType
import com.movie.app.util.PaginationResultState
import com.movie.app.util.notifyVisibleItems
import com.movie.app.util.setDefaultColor
import com.movie.app.util.setToolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity() {

    val viewModel: MainViewModel  by viewModel()
    private lateinit var adapter: MovieAdapter
    private lateinit var homeDrawer: HomeDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRefreshLayout()
        initRecyclerView()
        initToolbarSpinner()
        setToolbar(mainToolbar, R.string.app_name)
        homeDrawer = HomeDrawer(this, mainToolbar)
        emptyView.error().setOnClickListener { viewModel.subscribe() }
        viewModel.subscribe()

        viewModel.getFavStatusLiveData().observe(this, Observer {
            for (movie in adapter.data) {
                movie.isFav = it.contains(movie.id)
            }
            rvMovies.notifyVisibleItems()
        })
        viewModel.getSearchResultLiveData().observe(this, Observer {
            val result = it?.data
            when (it.status) {
                PaginationResultState.LOADING -> {
                    emptyView.showLoading()
                }
                PaginationResultState.FIRST_DATA -> {
                    val list = result?.results ?: return@Observer
                    Timber.i("showFirstData size:%s", list.size)
                    emptyView.showContent()
                    adapter.setNewData(list)
                    rvMovies.smoothScrollToPosition(0)
                    setLoadMore()
                    swipeLayoutMovies.isRefreshing = false
                    onDataCompleted(result.isFinished())
                }
                PaginationResultState.MORE_DATA -> {
                    val list = result?.results ?: return@Observer
                    Timber.i("showLoadMoreData size:%s", list.size)
                    adapter.addData(list)
                    swipeLayoutMovies.isRefreshing = false
                    onDataCompleted(result.isFinished())
                }
                PaginationResultState.NO_DATA -> {
                    emptyView.showEmpty()
                    swipeLayoutMovies.isRefreshing = false
                }
                PaginationResultState.ERROR -> {
                    if (result?.isLoadMore() == false) {
                        if (adapter.itemCount == 0) {
                            emptyView.showError()
                        }
                    } else {
                        adapter.loadMoreFail()
                    }
                    swipeLayoutMovies.isRefreshing = false
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (adapter.itemCount > 0) {
            viewModel.syncFavouritesStatues()
        }
    }

    private fun initRecyclerView() {
        adapter = MovieAdapter().apply {
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            setOnItemClickListener { _, _, position ->
                DetailsMovieActivity.startActivity(context, adapter.data[position].id)
            }
            setOnItemChildClickListener { _, _, position ->
                val movie = adapter.data[position]
                movie.isFav = !movie.isFav
                viewModel.addRemoveFavMovie(movie)
                adapter.notifyItemChanged(position)
            }
        }
        rvMovies.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        rvMovies.adapter = adapter
        setLoadMore()
    }

    private fun setLoadMore() {
        adapter.setOnLoadMoreListener({ viewModel.loadNextPage() }, rvMovies)
    }

    private fun initRefreshLayout() {
        swipeLayoutMovies.setDefaultColor()
        swipeLayoutMovies.setOnRefreshListener {
            adapter.setEnableLoadMore(false)
            viewModel.loadFirstPage()
        }
    }

    private fun initToolbarSpinner() {
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, MovieSortType.values())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        toolbarSpinner.adapter = adapter
        toolbarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                viewModel.onSearchFilterChanged(MovieSortType.values()[i])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }
    }

    private fun onDataCompleted(finished: Boolean) {
        Timber.i("onDataCompleted isFinished:%s", finished)
        if (finished) {
            adapter.loadMoreEnd(true)
        } else {
            adapter.loadMoreComplete()
            adapter.setEnableLoadMore(true)
        }
    }

    override fun onBackPressed() {
        if (homeDrawer.closeIfOpened()) {
            return
        }
        super.onBackPressed()
    }

}
