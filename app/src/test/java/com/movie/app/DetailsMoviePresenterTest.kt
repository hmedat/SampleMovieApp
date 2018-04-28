package com.movie.app

import com.movie.app.details.DetailsActivityContractor
import com.movie.app.details.DetailsMoviePresenter
import com.movie.app.interactors.IMoviesInteractor
import com.movie.app.modules.Movie
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
    private lateinit var moviesInteractor: IMoviesInteractor
    private lateinit var movie: Movie

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        schedulerProvider = ImmediateSchedulerProvider()
        presenter = DetailsMoviePresenter(schedulerProvider, moviesInteractor, view)
        movie = Movie().apply {
            id = 10
            title = "Avengers 01"
        }
        presenter.setMovieId(movie.id)
    }

    @Test
    fun testSubscribeIdNotSetTheMovieId() {
        val ioException = IOException()
        movie.id = 0
        presenter.setMovieId(movie.id)
        whenever(moviesInteractor.findMovie(0))
                .thenReturn(Observable.error(ioException))
        presenter.subscribe()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view).showError(ioException)
        verify(view, never()).showData(movie)
    }

    @Test
    fun testSubscribe() {
        whenever(moviesInteractor.findMovie(movie.id))
                .thenReturn(Observable.just(movie))
        presenter.subscribe()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view, never()).showError(IOException())
        verify(view).showData(movie)
    }
}
