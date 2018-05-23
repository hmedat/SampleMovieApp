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
    fun getMoviesWithNoInternetAndNoDataCached() {
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
    fun getMoviesWithInternetAndNoDataCached() {
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
        testSubscriber.assertNoErrors()
        assertEquals(testSubscriber.errorCount(), 0)
    }

    @Test
    fun getMoviesWithNoInternetAndWithDataCached() {
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

    @Test
    fun testGetMovieWithOutInternet() {
        val movie: Movie = Movie().apply {
            id = 1
            title = "Avengers"
        }
        val testSubscriber = TestObserver<Movie>()
        whenever(localRep.getMovie(movie.id))
            .thenReturn(Observable.just(movie))
        whenever(remoteRep.getMovie(movie.id))
            .thenReturn(Observable.error(networkException))

        movieRep.getMovie(movie.id).subscribe(testSubscriber)

        //Check if the db only deliver data
        assertEquals(testSubscriber.valueCount(), 1)
        assertNotNull(testSubscriber.values()[0])

        //Check if the network only deliver an error
        assertEquals(testSubscriber.errorCount(), 1)
        assertEquals(testSubscriber.errors()[0], networkException)
    }

    @Test
    fun testGetMovieWithInternet() {
        val movie: Movie = Movie().apply {
            id = 1
            title = "Avengers"
        }
        val testSubscriber = TestObserver<Movie>()
        whenever(localRep.getMovie(movie.id))
            .thenReturn(Observable.just(movie))
        whenever(remoteRep.getMovie(movie.id))
            .thenReturn(Observable.just(movie))

        movieRep.getMovie(movie.id).subscribe(testSubscriber)

        //Check if the db only deliver data
        assertEquals(testSubscriber.valueCount(), 2)
        assertNotNull(testSubscriber.values()[0])
        assertNotNull(testSubscriber.values()[1])

        //Check there are no errors
        testSubscriber.assertNoErrors()
        assertEquals(testSubscriber.errorCount(), 0)
    }
}
