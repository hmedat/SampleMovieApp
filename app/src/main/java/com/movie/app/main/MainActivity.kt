package com.movie.app.main

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.movie.app.BaseActivity
import com.movie.app.R
import com.movie.app.details.DetailsMovieActivity
import com.movie.app.modules.Movie
import com.movie.app.util.notifyVisibleItems
import com.movie.app.util.setDefaultColor
import com.movie.app.util.setToolbar
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), MainActivityContractor.View {

    @Inject
    lateinit var presenter: MainActivityContractor.Presenter
    private lateinit var adapter: MovieAdapter
    private lateinit var homeDrawer: HomeDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRefreshLayout()
        initRecyclerView()
        setToolbar(mainToolbar, R.string.app_name)
        homeDrawer = HomeDrawer(this, mainToolbar)
        emptyView.error().setOnClickListener { presenter.subscribe() }
        presenter.subscribe()
    }

    override fun onResume() {
        super.onResume()
        if (adapter.itemCount > 0) {
            presenter.syncFavouritesStatues()
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
                presenter.addRemoveFavMovie(movie)
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
        adapter.setOnLoadMoreListener({ presenter.loadNextPage() }, rvMovies)
    }

    private fun initRefreshLayout() {
        swipeLayoutMovies.setDefaultColor()
        swipeLayoutMovies.setOnRefreshListener {
            adapter.setEnableLoadMore(false)
            presenter.loadFirstPage()
        }
    }

    override fun showProgressBar() {
        emptyView.showLoading()
    }

    override fun hideProgressBar() {
        swipeLayoutMovies.isRefreshing = false
    }

    override fun showNoData() {
        Timber.i("showNoData")
        emptyView.showEmpty()
    }

    override fun showFirstData(data: List<Movie>) {
        Timber.i("showFirstData size:%s", data.size)
        emptyView.showContent()
        adapter.setNewData(data)
        setLoadMore()
    }

    override fun showLoadMoreData(data: List<Movie>) {
        Timber.i("showLoadMoreData size:%s", data.size)
        adapter.addData(data)
    }

    override fun onDataCompleted(finished: Boolean) {
        Timber.i("onDataCompleted isFinished:%s", finished)
        if (finished) {
            adapter.loadMoreEnd(true)
        } else {
            adapter.loadMoreComplete()
            adapter.setEnableLoadMore(true)
        }
    }

    override fun showError(isFirstPage: Boolean, throwable: Throwable) {
        Timber.e(throwable, "isFirstPage: %s, AdapterItemsSize: %s", isFirstPage, adapter.itemCount)
        if (isFirstPage) {
            if (adapter.itemCount == 0) {
                emptyView.showError()
            }
        } else {
            adapter.loadMoreFail()
        }
    }

    override fun updateFavouritesStatues(list: HashSet<Long>) {
        for (movie in adapter.data) {
            movie.isFav = list.contains(movie.id)
        }
    }

    override fun onBackPressed() {
        if (homeDrawer.closeIfOpened()) {
            return
        }
        super.onBackPressed()
    }

    override fun notifyVisibleItems() {
        rvMovies.notifyVisibleItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unSubscribe()
    }
}
