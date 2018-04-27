package com.movie.app

import com.movie.app.api.ApiInterface
import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.interactors.IMoviesInteractor
import com.movie.app.interactors.MoviesInteractor
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class MovieInteractorTest {

    private lateinit var moviesInteractor: IMoviesInteractor
    @Mock
    private lateinit var apiInterface: ApiInterface

    @Mock
    private lateinit var movieSearchFilter: MovieSearchFilter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        moviesInteractor = MoviesInteractor(apiInterface)
    }

    @Test
    fun testFindMovie() {
        val movie: Movie = Movie().apply {
            id = 1
            title = "Avengers"
        }
        val testSubscriber = TestObserver<Movie>()
        whenever(apiInterface.findMovie(any(Long::class.java))).thenReturn(Observable.just(movie))
        moviesInteractor.findMovie(movie.id).subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertComplete()
        testSubscriber.assertValue(movie)
    }

    @Test
    fun testFailureFindMovie() {
        val exception = IllegalArgumentException()
        val testSubscriber = TestObserver<Movie>()
        whenever(apiInterface.findMovie(any(Long::class.java))).thenReturn(Observable.error(exception))
        moviesInteractor.findMovie(10).subscribe(testSubscriber)
        testSubscriber.assertError(exception)
        testSubscriber.assertNotComplete()
    }

    @Test
    fun testGetMovies() {
        val result = LatestMoviesResult()
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
        val testSubscriber = TestObserver<LatestMoviesResult>()
        whenever(apiInterface.getLatestMovies(any(Int::class.java)))
                .thenReturn(Observable.just(result))
        moviesInteractor.getLatest(movieSearchFilter).subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertComplete()
        testSubscriber.assertValue(result)
    }


    @Test
    fun testFailureGetMovies() {
        val exception = IllegalArgumentException()
        val testSubscriber = TestObserver<LatestMoviesResult>()
        whenever(apiInterface.getLatestMovies(any(Int::class.java)))
                .thenReturn(Observable.error(exception))
        moviesInteractor.getLatest(movieSearchFilter).subscribe(testSubscriber)
        testSubscriber.assertError(exception)
        testSubscriber.assertNotComplete()
    }
}
