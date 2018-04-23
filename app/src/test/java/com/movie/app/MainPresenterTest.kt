package com.movie.app

import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.interactors.IMoviesInteractor
import com.movie.app.main.MainActivityContractor
import com.movie.app.main.MainPresenter
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.util.schedulers.BaseSchedulerProvider
import com.movie.app.util.schedulers.ImmediateSchedulerProvider
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class MainPresenterTest {


    private lateinit var schedulerProvider: BaseSchedulerProvider
    @Mock
    private lateinit var view: MainActivityContractor.View
    @Mock
    private lateinit var moviesInteractor: IMoviesInteractor
    @Mock
    private lateinit var movieSearchFilter: MovieSearchFilter

    private lateinit var presenter: MainActivityContractor.Presenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        schedulerProvider = ImmediateSchedulerProvider()
        presenter = MainPresenter(schedulerProvider, moviesInteractor, view, movieSearchFilter)

    }

    @Test
    fun loadFirstPageIntoView() {
        val movie01: Movie = Movie().apply {
            id = 1
            title = "Avengers 01"
        }
        val movie02: Movie = Movie().apply {
            id = 2
            title = "Avengers 02"
        }
        val movie03: Movie = Movie().apply {
            id = 3
            title = "Avengers 03"
        }

        val result = LatestMoviesResult()
        result.results = listOf(movie01, movie02, movie03)
        whenever(movieSearchFilter.pageNumber)
                .thenReturn(MovieSearchFilter.First_PAGE)
        whenever(movieSearchFilter.isFirstPage())
                .thenReturn(true)
        whenever(moviesInteractor.getLatest(movieSearchFilter))
                .thenReturn(Observable.just(result))

        presenter.loadFirstPage()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
    }

}
