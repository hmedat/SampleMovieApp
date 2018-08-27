package com.movie.app

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.repositories.MovieRepository
import com.movie.app.repositories.local.LocalMovieRepository
import com.movie.app.repositories.remote.RemoteMovieRepository
import com.movie.app.rx.TestHelper
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Observer
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException

class MovieRepositoryTest {

    @Mock
    private lateinit var localRep: LocalMovieRepository
    @Mock
    private lateinit var remoteRep: RemoteMovieRepository

    private lateinit var movieRep: MovieRepository

    @Mock
    private lateinit var networkException: IOException
    @Mock
    private lateinit var diskException: IOException
    private lateinit var observer: Observer<MoviesResult>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        movieRep = MovieRepository(localRep, remoteRep)
        observer = TestHelper.mockObserver()
    }

    /**
     * Test Case:
     * No Result from internet and No Result from database
     */
    @Test
    fun testGetMoviesWithEmptyResultsFromInternetAndDatabase() {
        val searchFilter = MovieSearchFilter(pageNumber = 1)
        whenever(localRep.getMovies(searchFilter)).thenReturn(Observable.just(MoviesResult()))
        whenever(remoteRep.getMovies(searchFilter)).thenReturn(Observable.just(MoviesResult()))

        movieRep.getMovies(searchFilter)
            .test()
            .assertValueCount(2)
            .assertValueAt(0) { it.isEmptyData() }
            .assertValueAt(1) { it.isEmptyData() }
            .assertNoErrors()
    }

    /**
     * Test Case:
     * An Error occurs from internet and No Result from database
     */
    @Test
    fun testGetMoviesWithErrorResultsFromInternetAndNoDataFromDatabase() {
        val searchFilter = MovieSearchFilter(pageNumber = 1)
        whenever(localRep.getMovies(searchFilter)).thenReturn(Observable.just(MoviesResult()))
        whenever(remoteRep.getMovies(searchFilter)).thenReturn(Observable.error(networkException))

        movieRep.getMovies(searchFilter)
            .test()
            .assertValueCount(1)
            .assertValueAt(0) { it.isEmptyData() }
            .assertError(IOException::class.java)
    }

    /**
     * Test Case:
     * An Error occurs from database and No Result from Internet
     */
    @Test
    fun testGetMoviesWithErrorResultsFromDatabaseAndNoDataFromInternet() {
        val searchFilter = MovieSearchFilter(pageNumber = 1)
        whenever(remoteRep.getMovies(searchFilter)).thenReturn(Observable.just(MoviesResult()))
        whenever(localRep.getMovies(searchFilter)).thenReturn(Observable.error(networkException))

        movieRep.getMovies(searchFilter)
            .test()
            .assertValueCount(2)
            .assertValueAt(0) { it.isEmptyData() }
            .assertValueAt(1) { it.isEmptyData() }
            .assertNoErrors()
    }

    /**
     * Test Case:
     * Verify if the result from network call saved in the database
     */
    @Test
    fun testGetMoviesFromNetworkAndSaveThemInDatabase() {
        val searchFilter = MovieSearchFilter(pageNumber = 1)
        val networkResult = MoviesResult(results = arrayListOf(Movie(1), Movie(2)))
        whenever(localRep.getMovies(searchFilter)).thenReturn(Observable.just(MoviesResult()))
        whenever(remoteRep.getMovies(searchFilter)).thenReturn(Observable.just(networkResult))

        movieRep.getMovies(searchFilter)
            .test()
            .assertNoErrors()
            .assertValueCount(2)
            .assertOf {
                networkResult.results?.let {
                    verify(localRep).insertMovies(it)
                }
            }
    }

    /**
     * Test Case:
     * Verify if the result from database received to the called
     */
    @Test
    fun testGetMoviesWithNoInternetAndWithDataCached() {
        val searchFilter = MovieSearchFilter(pageNumber = 1)
        val localResult = MoviesResult(results = arrayListOf(Movie(1), Movie(2)))

        whenever(localRep.getMovies(searchFilter)).thenReturn(Observable.just(localResult))
        whenever(remoteRep.getMovies(searchFilter)).thenReturn(Observable.error(networkException))

        movieRep.getMovies(searchFilter)
            .test()
            .assertError(IOException::class.java)
            .assertValueAt(0) { it.results?.size == localResult.results?.size }
    }

    /**
     * Test Case:
     * No Result from internet and page number is 2
     */
    @Test
    fun testGetMoviesLoadMoreWithEmptyResultsFromInternet() {
        val searchFilter = MovieSearchFilter(pageNumber = 2)
        whenever(remoteRep.getMovies(searchFilter)).thenReturn(Observable.just(MoviesResult()))
        movieRep.getMovies(searchFilter)
            .test()
            .assertValueCount(1)
            .assertValueAt(0) { it.isEmptyData() }
            .assertNoErrors()
    }

    /**
     * Test Case:
     * An Error occurs from internet and page number is 2
     */
    @Test
    fun testGetMoviesLoadMoreWithErrorResultsFromInternet() {
        val searchFilter = MovieSearchFilter(pageNumber = 2)
        whenever(remoteRep.getMovies(searchFilter)).thenReturn(Observable.error(networkException))
        movieRep.getMovies(searchFilter)
            .test()
            .assertValueCount(0)
            .assertError(IOException::class.java)
    }

    /**
     * Test Case:
     * Movie Not Found in both (Network and Database)
     */
    @Test
    fun testGetMovieWithEmptyResultFromInternetAndDatabase() {
        val movieId = 10L
        whenever(localRep.getMovie(movieId)).thenReturn(Observable.error(diskException))
        whenever(remoteRep.getMovie(movieId)).thenReturn(Observable.error(networkException))

        movieRep.getMovie(movieId)
            .test()
            .assertValueCount(0)
            .assertError(IOException::class.java)
    }

    /**
     * Test Case:
     * Movie Found in Database and didn't find in Network
     */
    @Test
    fun testGetMovieWithErrorResultsFromInternetAndGetDataFromDatabase() {
        val movie = Movie(19)
        whenever(localRep.getMovie(movie.id)).thenReturn(Observable.just(movie))
        whenever(remoteRep.getMovie(movie.id)).thenReturn(Observable.error(networkException))

        movieRep.getMovie(movie.id)
            .test()
            .assertValueCount(1)
            .assertError(IOException::class.java)
    }

    /**
     * Test Case:
     * Movie Found in Database and  Network
     */
    @Test
    fun testGetMovieFromInternetAndGetDataFromDatabase() {
        val movie = Movie(19)
        whenever(localRep.getMovie(movie.id)).thenReturn(Observable.just(movie))
        whenever(remoteRep.getMovie(movie.id)).thenReturn(Observable.just(movie))

        movieRep.getMovie(movie.id)
            .test()
            .assertValueCount(2)
            .assertNoErrors()
    }

    /**
     * Test Case:
     * Get Fav Movie
     */
    @Test
    fun testGetFavMovies() {
        val list = ArrayList<Movie>()
        whenever(localRep.getFavMovies()).thenReturn(Observable.just(MoviesResult(results = list)))
        movieRep.getFavMovies()
            .test()
            .assertValueCount(1)
            .assertValue {
                it.results?.isEmpty()!!
            }
            .assertNoErrors()
    }

    /**
     * Test Case:
     * Get Fav Movie
     */
    @Test
    fun testRemoveAddFavMovie() {
        val movie = Movie(11)
        whenever(localRep.removeAddFavMovie(movie.id, true)).thenReturn(Observable.just(1))
        movieRep.removeAddFavMovie(movie.id, true)
            .test()
            .assertValueCount(1)
            .assertValue {
                it == 1
            }
            .assertNoErrors()
    }
}
