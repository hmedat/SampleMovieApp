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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException

class MovieRepositoryTest {

    @Mock
    private lateinit var localRep: MovieDataSource
    @Mock
    private lateinit var remoteRep: RemoteMovieRepository

    private lateinit var movieRep: MovieRepository

    @Mock
    private lateinit var networkException: IOException
    @Mock
    private lateinit var diskException: IOException

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
            title = "Avengers 03"
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

    @Test
    fun getFirstPageWithNoInternetAndNoDataCached() {
        val searchFilter = MovieSearchFilter()
        searchFilter.pageNumber = 1

        val testSubscriber = TestObserver<MoviesResult>()
        whenever(localRep.getMovies(searchFilter))
            .thenReturn(Observable.error(diskException))

        whenever(remoteRep.getMovies(searchFilter))
            .thenReturn(Observable.error(networkException))

        movieRep.getMovies(searchFilter).subscribe(testSubscriber)

        //Check if the db only deliver data
        assertEquals(testSubscriber.valueCount(), 1)
        assertNotNull(testSubscriber.values()[0])
        assertNotEquals(testSubscriber.errors()[0], diskException)
        //Check if the network only deliver an error
        assertEquals(testSubscriber.errorCount(), 1)
        assertEquals(testSubscriber.errors()[0], networkException)
    }

    @Test
    fun getFirstPageWithInternetAndNoDataCached() {
        val searchFilter = MovieSearchFilter()
        searchFilter.pageNumber = 1

        val testSubscriber = TestObserver<MoviesResult>()
        whenever(localRep.getMovies(searchFilter))
            .thenReturn(Observable.error(diskException))

        whenever(remoteRep.getMovies(searchFilter))
            .thenReturn(Observable.just(MoviesResult()))

        movieRep.getMovies(searchFilter).subscribe(testSubscriber)

        //Check if the db only deliver data
        assertEquals(testSubscriber.valueCount(), 2)
        assertNotNull(testSubscriber.values()[0])
        assertNotNull(testSubscriber.values()[1])

        //Check there are no errors
        assertEquals(testSubscriber.errorCount(), 0)
    }

    @Test
    fun getFirstPageWithNoInternetAndWithDataCached() {
        val searchFilter = MovieSearchFilter()
        searchFilter.pageNumber = 1

        val testSubscriber = TestObserver<MoviesResult>()
        whenever(localRep.getMovies(searchFilter))
            .thenReturn(Observable.just(MoviesResult()))

        whenever(remoteRep.getMovies(searchFilter))
            .thenReturn(Observable.error(networkException))

        movieRep.getMovies(searchFilter).subscribe(testSubscriber)

        //Check if the db only deliver data
        assertEquals(testSubscriber.valueCount(), 1)
        assertNotNull(testSubscriber.values()[0])

        //Check if the network only deliver an error
        assertEquals(testSubscriber.errorCount(), 1)
        assertEquals(testSubscriber.errors()[0], networkException)
    }
}
