package com.movie.app

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.repositories.MovieDataSource
import com.movie.app.repositories.MovieRepository
import com.movie.app.repositories.remote.RemoteMovieRepository
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MovieRepositoryTest {

    @Mock
    private lateinit var localRep: MovieDataSource
    @Mock
    private lateinit var remoteRep: RemoteMovieRepository

    private lateinit var movieRep: MovieRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        movieRep = MovieRepository(localRep, remoteRep)
    }

    @Test
    fun testGetMovie() {
        val movie: Movie = Movie().apply {
            id = 1
            title = "Avengers"
        }
        val testSubscriber = TestObserver<Movie>()
        whenever(localRep.getMovie(ArgumentMatchers.any(Long::class.java)))
                .thenReturn(Observable.just(movie))
        whenever(remoteRep.getMovie(ArgumentMatchers.any(Long::class.java)))
                .thenReturn(Observable.just(movie))
        movieRep.getMovie(movie.id).subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertComplete()
        assertEquals(testSubscriber.values()[0], movie)
        assertEquals(testSubscriber.values()[1], movie)
    }

    @Test
    fun testGetMovies() {
        val searchFilter = MovieSearchFilter()
        searchFilter.pageNumber = 1
        val latestMoviesResult = MoviesResult()
        latestMoviesResult.results = listOf(Movie().apply {
            id = 1
            title = "Avengers 01"
        }, Movie().apply {
            id = 2
            title = "Avengers 02"
        }, Movie().apply {
            id = 3
            title = "Avenge§rs 03"
        })
        val testSubscriber = TestObserver<MoviesResult>()
        whenever(localRep.getMovies(searchFilter))
                .thenReturn(Observable.just(latestMoviesResult))
        whenever(remoteRep.getMovies(searchFilter))
                .thenReturn(Observable.just(latestMoviesResult))

        movieRep.getMovies(searchFilter).subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertComplete()

        assertEquals(testSubscriber.values()[0], latestMoviesResult)
        assertEquals(testSubscriber.values()[1], latestMoviesResult)
    }
}
