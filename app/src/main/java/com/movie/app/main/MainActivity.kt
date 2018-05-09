package com.movie.app.main

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.movie.app.BaseActivity
import com.movie.app.R
import com.movie.app.api.result.MoviesResult
import com.movie.app.details.DetailsMovieActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainActivityContractor.View {

    @Inject
    lateinit var presenter: MainActivityContractor.Presenter
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRefreshLayout()
        initRecyclerView()
        presenter.subscribe()
    }

    private fun initRecyclerView() {
        adapter = MovieAdapter().apply {
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            setOnItemClickListener { _, _, position ->
                val movie = adapter.data[position]
                DetailsMovieActivity.startActivity(context, movie.id)
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
        swipeLayoutMovies.setColorSchemeColors(Color.rgb(47, 223, 189))
        swipeLayoutMovies.setOnRefreshListener({
            adapter.setEnableLoadMore(false)
            presenter.loadFirstPage()
        })
    }

    override fun showProgressBar() {
        progressView.showLoading()
        swipeLayoutMovies.isRefreshing = true
    }

    override fun hideProgressBar() {
        swipeLayoutMovies.isRefreshing = false
    }

    override fun showNoData() {
        progressView.showEmpty(R.drawable.ic_cart_24dp_white, getString(R.string.title_no_data)
                , getString(R.string.desc_no_data));
    }

    override fun showData(result: MoviesResult) {
        val list = result.results
        if (list!!.isNotEmpty()) {
            progressView.showContent()
            if (result.isLoadMore()) {
                adapter.addData(list)
            } else {
                adapter.setNewData(list)
                setLoadMore()
                rvMovies.smoothScrollToPosition(0)
            }
        }
        if (result.isFinished()) {
            adapter.loadMoreEnd(true)
        } else {
            adapter.loadMoreComplete()
            adapter.setEnableLoadMore(true)
        }
    }

    override fun showError(isFirstPage: Boolean, throwable: Throwable) {
        if (isFirstPage) {
            if (adapter.itemCount > 0) {
                return
            }
            progressView.showError(R.drawable.ic_no_connection_24dp_white
                    , getString(R.string.title_no_connection)
                    , getString(R.string.desc_no_connection)
                    , getString(R.string.btn_no_connection)) { presenter.loadFirstPage() }
        } else {
            adapter.loadMoreFail()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unSubscribe()
    }
}
