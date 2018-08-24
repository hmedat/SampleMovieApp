package com.movie.app

import com.movie.app.api.result.MoviesResult
import com.movie.app.main.MainActivityContractor
import com.movie.app.main.MainPresenter
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.repositories.MovieDataSource
import com.movie.app.util.schedulers.BaseSchedulerProvider
import com.movie.app.util.schedulers.ImmediateSchedulerProvider
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException
class MainPresenterTest {

    private lateinit var schedulerProvider: BaseSchedulerProvider
    @Mock
    private lateinit var view: MainActivityContractor.View
    @Mock
    private lateinit var movieDataSource: MovieDataSource
    @Mock
    private lateinit var movieSearchFilter: MovieSearchFilter

    private lateinit var presenter: MainActivityContractor.Presenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        schedulerProvider = ImmediateSchedulerProvider()
        presenter = MainPresenter(schedulerProvider, movieDataSource, view, movieSearchFilter)
    }

    @Test
    fun getFirstPageForFirstRunWithNoInternetAndNoDataCached() {
        val result = MoviesResult()
        result.page = MovieSearchFilter.First_PAGE
        result.results = listOf(Movie().apply {
            id = 1
            title = "Avengers 01"
        }, Movie().apply {
            id = 2
            title = "Avengers 02"
        }, Movie().apply {
            id = 3
            title = "Avengers 03"
        })
        whenever(movieSearchFilter.pageNumber)
                .thenReturn(MovieSearchFilter.First_PAGE)
        whenever(movieSearchFilter.isFirstPage())
                .thenReturn(true)
        whenever(movieDataSource.getMovies(movieSearchFilter))
                .thenReturn(Observable.just(result))
        presenter.loadFirstPage()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view, never()).showNoData()
        verify(view, never()).showError(true, IllegalAccessException())
        verify(view).showFirstData(result.results!!)
    }

    @Test
    fun loadFirstPageIntoView() {
        val result = MoviesResult()
        result.page = MovieSearchFilter.First_PAGE
        result.results = listOf(Movie().apply {
            id = 1
            title = "Avengers 01"
        }, Movie().apply {
            id = 2
            title = "Avengers 02"
        }, Movie().apply {
            id = 3
            title = "Avengers 03"
        })
        whenever(movieSearchFilter.pageNumber)
                .thenReturn(MovieSearchFilter.First_PAGE)
        whenever(movieSearchFilter.isFirstPage())
                .thenReturn(true)
        whenever(movieDataSource.getMovies(movieSearchFilter))
                .thenReturn(Observable.just(result))
        presenter.loadFirstPage()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view, never()).showNoData()
        verify(view, never()).showError(true, IllegalAccessException())
        verify(view).showFirstData(result.results!!)
    }

    @Test
    fun loadFirstPageIntoViewWithEmptyData() {
        val result = MoviesResult()
        result.page = MovieSearchFilter.First_PAGE
        whenever(movieSearchFilter.pageNumber)
                .thenReturn(MovieSearchFilter.First_PAGE)
        whenever(movieSearchFilter.isFirstPage())
                .thenReturn(true)
        whenever(movieDataSource.getMovies(movieSearchFilter))
                .thenReturn(Observable.just(result))
        presenter.loadFirstPage()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view, never()).showError(true, IllegalAccessException())
        verify(view).showNoData()
    }

    @Test
    fun loadFirstPageIntoViewWithError() {
        val result = MoviesResult()
        val ioException = IOException()
        result.page = MovieSearchFilter.First_PAGE
        whenever(movieSearchFilter.pageNumber)
                .thenReturn(MovieSearchFilter.First_PAGE)
        whenever(movieSearchFilter.isFirstPage())
                .thenReturn(true)
        whenever(movieDataSource.getMovies(movieSearchFilter))
                .thenReturn(Observable.error(ioException))
        presenter.loadFirstPage()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view).showError(true, ioException)
        verify(view, never()).showNoData()
        verify(view, never()).showFirstData(ArrayList())
    }

    @Test
    fun loadNextPageIntoView() {
        val pageNumber = 2
        val result = MoviesResult()
        result.page = pageNumber
        result.results = listOf(Movie().apply {
            id = 1
            title = "Avengers 04"
        }, Movie().apply {
            id = 2
            title = "Avengers 05"
        }, Movie().apply {
            id = 3
            title = "Avengers 06"
        })
        whenever(movieSearchFilter.pageNumber)
                .thenReturn(pageNumber)
        whenever(movieSearchFilter.isFirstPage())
                .thenReturn(false)
        whenever(movieDataSource.getMovies(movieSearchFilter))
                .thenReturn(Observable.just(result))
        presenter.loadNextPage()
        verify(view, never()).showProgressBar()
        verify(view).hideProgressBar()
        verify(view, never()).showNoData()
        verify(view, never()).showError(true, IllegalAccessException())
        verify(view).showLoadMoreData(result.results!!)
    }

    @Test
    fun loadNextPageIntoViewWithEmptyData() {
        val pageNumber = 2
        val result = MoviesResult()
        result.results = listOf(Movie().apply {
            id = 1
            title = "Avengers 04"
        }, Movie().apply {
            id = 2
            title = "Avengers 05"
        }, Movie().apply {
            id = 3
            title = "Avengers 06"
        })
        result.page = pageNumber
        whenever(movieSearchFilter.pageNumber)
                .thenReturn(pageNumber)
        whenever(movieDataSource.getMovies(movieSearchFilter))
                .thenReturn(Observable.just(result))
        presenter.loadNextPage()
        verify(view, never()).showProgressBar()
        verify(view).hideProgressBar()
        verify(view, never()).showNoData()
        verify(view, never()).showError(true, IllegalAccessException())
        verify(view).showLoadMoreData(result.results!!)
    }

    @Test
    fun loadNextPageIntoViewWithError() {
        val ioException = IOException()
        val pageNumber = 2
        val result = MoviesResult()
        result.page = pageNumber
        whenever(movieSearchFilter.pageNumber)
                .thenReturn(pageNumber)
        whenever(movieSearchFilter.isFirstPage())
                .thenReturn(false)
        whenever(movieDataSource.getMovies(movieSearchFilter))
                .thenReturn(Observable.error(ioException))
        presenter.loadNextPage()
        verify(view, never()).showProgressBar()
        verify(view).hideProgressBar()
        verify(view, never()).showNoData()
        verify(view).showError(false, ioException)
        verify(view, never()).showLoadMoreData(ArrayList())
    }
}
