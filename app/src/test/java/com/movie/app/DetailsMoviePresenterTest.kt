package com.movie.app

import com.movie.app.api.ApiInterface
import com.movie.app.api.result.MoviesResult
import com.movie.app.details.DetailsActivityContractor
import com.movie.app.details.DetailsMoviePresenter
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

class DetailsMoviePresenterTest {

    private lateinit var presenter: DetailsActivityContractor.Presenter
    private lateinit var schedulerProvider: BaseSchedulerProvider
    @Mock
    private lateinit var view: DetailsActivityContractor.View
    @Mock
    private lateinit var movieDataSource: MovieDataSource
    @Mock
    private lateinit var apiInterface: ApiInterface
    private lateinit var movie: Movie

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        schedulerProvider = ImmediateSchedulerProvider()
        presenter = DetailsMoviePresenter(schedulerProvider, movieDataSource, apiInterface, view)
        movie = Movie().apply {
            id = 10
            title = "Avengers 01"
        }
        presenter.setMovieId(movie.id)
    }

    @Test
    fun testSubscribe() {
        whenever(movieDataSource.getMovie(movie.id))
                .thenReturn(Observable.just(movie))
        whenever(apiInterface.getSimilarMovies(movie.id))
                .thenReturn(Observable.just(MoviesResult()))

        presenter.subscribe()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view, never()).showError(IOException())
        verify(view).showData(movie)
    }

    @Test
    fun testGetSimilarMovie() {
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
        whenever(apiInterface.getSimilarMovies(movie.id))
                .thenReturn(Observable.just(result))
        presenter.getSimilarMovies()

        verify(view, never()).showProgressBar()
        verify(view, never()).hideProgressBar()
        verify(view, never()).showError(IOException())
        verify(view).showSimilarMovies(result.results!!)
    }
}
