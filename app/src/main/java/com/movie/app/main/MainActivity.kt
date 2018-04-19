package com.movie.app.main

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.movie.app.BaseActivity
import com.movie.app.R
import com.movie.app.api.result.LatestMoviesResult
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), MainActivityContractor.View {

    private lateinit var presenter: MainActivityContractor.Presenter
    private lateinit var adapter: MovieAdapter
    private lateinit var errorView: View
    private lateinit var notDataView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRefreshLayout()
        initRecyclerView()
        initErrorView()
        initNoDataView()
        presenter = MainPresenter(Schedulers.io(), this)
        presenter.subscribe()
    }

    private fun initRecyclerView() {
        rvMovies.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter()
        adapter.setOnLoadMoreListener({ presenter.loadNextPage() }, rvMovies)
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
        rvMovies.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            presenter.onMovieClicked(adapter.data[position])
        }
    }

    private fun initRefreshLayout() {
        swipeLayoutMovies.setColorSchemeColors(Color.rgb(47, 223, 189))
        swipeLayoutMovies.setOnRefreshListener({
            adapter.setEnableLoadMore(false)
            presenter.loadFirstPage()
        })
    }

    private fun initErrorView() {
        errorView = layoutInflater.inflate(R.layout.layout_error, rvMovies.parent as ViewGroup, false)
        errorView.setOnClickListener { presenter.loadFirstPage() }
    }

    private fun initNoDataView() {
        notDataView = layoutInflater.inflate(R.layout.layout_no_data, rvMovies.parent as ViewGroup, false)
        notDataView.setOnClickListener { presenter.loadFirstPage() }
    }

    override fun showProgressBar() {
        swipeLayoutMovies.isRefreshing = true
    }

    override fun hideProgressBar() {
        swipeLayoutMovies.isRefreshing = false
    }

    override fun showNoData() {
        adapter.emptyView = notDataView
    }

    override fun showData(result: LatestMoviesResult) {
        adapter.addData(result.results!!)
        if (result.isFinshed()) {
            adapter.loadMoreEnd(true)
        } else {
            adapter.loadMoreComplete()
            adapter.setEnableLoadMore(true)
        }
    }

    override fun showError(isFirstPage: Boolean, throwable: Throwable) {
        if (isFirstPage) {
            adapter.emptyView = errorView
        } else {
            adapter.loadMoreFail()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unSubscribe()
    }
}
