package com.movie.app

import com.movie.app.api.ApiInterface
import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.interactors.IMoviesInteractor
import com.movie.app.interactors.MoviesInteractor
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class TestMovieInteractor {

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
            id = 1010
            title = "Avengers"
        }
        val testSubscriber = TestObserver<Movie>()
        `when`(apiInterface.findMovie(any(Long::class.java))).thenReturn(Observable.just(movie))
        moviesInteractor.findMovie(movie.id).subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(movie)
    }

    @Test
    fun testGetMovies() {
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

        val testSubscriber = TestObserver<LatestMoviesResult>()
        `when`(apiInterface.getLatestMovies(any(Int::class.java)))
                .thenReturn(Observable.just(result))

        moviesInteractor.getLatest(movieSearchFilter).subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(result)
    }
}
