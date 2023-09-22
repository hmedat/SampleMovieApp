package com.movie.app

import com.movie.app.modules.MovieSearchFilter
import com.movie.app.repositories.MovieDataSource
import com.movie.app.repositories.MovieRepository
import com.movie.app.repositories.RemoteMovieDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MovieRepositoryTest {

    @MockK
    lateinit var localRep: MovieDataSource
    @MockK
    lateinit var remoteRep: RemoteMovieDataSource
    private lateinit var movieRep: MovieRepository

    @MockK
    private lateinit var networkException: IOException
    @MockK
    private lateinit var diskException: IOException

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        movieRep = MovieRepository(localRep, remoteRep)
        /*       MockitoAnnotations.initMocks(this)
               observer = TestHelper.mockObserver()*/
    }

    @Test
    fun getMoviesWithNoInternetAndNoDataCached() {
        val searchFilter = MovieSearchFilter()
        searchFilter.pageNumber = 1
        //  every { runBlocking { localRep.getMovies(searchFilter) } } throws diskException
        coEvery { movieRep.getRemoteMovies(searchFilter) } throws networkException

        runBlocking {
            val remoteMovies = movieRep.getRemoteMovies(searchFilter)
            val x = 10
            remoteMovies
        }
        /*    movieRep.getMovies(searchFilter)
                .test()
                .assertValueCount(1)
                .assertError(Throwable::class.java)*/
        // Check if the network only deliver an error
        // Check if the db only deliver data
    }
    /*
      @Test
      fun getMoviesWithInternetAndNoDataCached() {
          val searchFilter = MovieSearchFilter()
          searchFilter.pageNumber = 1

          whenever(localRep.getMovies(searchFilter))
              .thenReturn(Observable.error(diskException))

          whenever(remoteRep.getMovies(searchFilter))
              .thenReturn(Observable.just(MoviesResult()))

          movieRep.getMovies(searchFilter)
              .test()
              .assertNoErrors()
              .assertValueCount(2)
          // Check if the db and network  deliver data with no errors
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

          // Check if the db only deliver data
          // Check if the network only deliver an error
          movieRep.getMovies(searchFilter)
              .test()
              .assertError(IOException::class.java)
              .assertValueCount(1)
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

          // Check if the db only deliver data
          Assert.assertEquals(testSubscriber.valueCount(), 1)
          Assert.assertNotNull(testSubscriber.values()[0])

          // Check if the network only deliver an error
          Assert.assertEquals(testSubscriber.errorCount(), 1)
          Assert.assertEquals(testSubscriber.errors()[0], networkException)
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

          // Check if the db only deliver data
          Assert.assertEquals(testSubscriber.valueCount(), 2)
          Assert.assertNotNull(testSubscriber.values()[0])
          Assert.assertNotNull(testSubscriber.values()[1])

          // Check there are no errors
          testSubscriber.assertNoErrors()
          Assert.assertEquals(testSubscriber.errorCount(), 0)
      }*/
}
