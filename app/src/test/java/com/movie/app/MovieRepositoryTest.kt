package com.movie.app

import com.movie.app.modules.Movie
import com.movie.app.repositories.LocalMovieRepository
import com.movie.app.repositories.MovieRepository
import com.movie.app.repositories.MovieRepositoryImp
import com.movie.app.repositories.RemoteMovieRepository
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
    private lateinit var localRep: LocalMovieRepository
    @Mock
    private lateinit var remoteRep: RemoteMovieRepository

    private lateinit var movieRep: MovieRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        movieRep = MovieRepositoryImp(localRep, remoteRep)
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
        val movies = listOf(Movie().apply {
            id = 1
            title = "Avengers 01"
        }, Movie().apply {
            id = 2
            title = "Avengers 02"
        }, Movie().apply {
            id = 3
            title = "Avengers 03"
        })
        val testSubscriber = TestObserver<List<Movie>>()
        whenever(localRep.getMovies())
                .thenReturn(Observable.just(movies))
        whenever(remoteRep.getMovies())
                .thenReturn(Observable.just(movies))
        movieRep.getMovies().subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertComplete()
        assertEquals(testSubscriber.values()[0], movies)
        assertEquals(testSubscriber.values()[1], movies)
    }
}