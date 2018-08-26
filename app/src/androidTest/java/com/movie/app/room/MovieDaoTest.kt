package com.movie.app.room

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var movieDao: MovieDao
    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            AppDatabase::class.java
        ).build()
        movieDao = database.movieDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetMoviesOrderByPopularity() {
        movieDao.getMoviesOrderByPopularity(20)
            .test()
            .assertValueCount(0)
            .assertNoErrors()
    }

    @Test
    fun testGetMoviesOrderByReleaseDate() {
        movieDao.getMoviesOrderByReleaseDate(20)
            .test()
            .assertValueCount(0)
            .assertNoErrors()
    }

    @Test
    fun testGetFavMovies() {
        val listOfMovies = arrayListOf(
            Movie(100, isFav = true), Movie(3), Movie(6)
        )
        movieDao.insert(listOfMovies)
        movieDao.getFavMovies()
            .test()
            .awaitCount(1)
            .assertValueCount(1)
    }

    @Test
    fun testGetFavMovieIds() {
        val listOfMovies = arrayListOf(
            Movie(100, isFav = true), Movie(3, isFav = true), Movie(6)
        )
        movieDao.insert(listOfMovies)
        val favMovieIds = movieDao.getFavMovieIds()
        assertEquals(favMovieIds.size, 2)
    }

    @Test
    fun testGetMovie() {
        val listOfMovies = arrayListOf(
            Movie(100), Movie(3), Movie(6)
        )
        movieDao.insert(listOfMovies)
        movieDao.getMovie(100)
            .test()
            .assertValueCount(1)
    }

    @Test
    fun testGetMovieNotExist() {
        val listOfMovies = arrayListOf(
            Movie(100), Movie(3), Movie(6)
        )
        movieDao.insert(listOfMovies)
        movieDao.getMovie(1)
            .test()
            .assertValueCount(0)
            .assertNoErrors()
    }

    @Test
    fun testInsertMovies() {
        val listOfMovies = arrayListOf(
            Movie(100), Movie(3), Movie(6)
        )
        val insertedMovies = movieDao.insert(listOfMovies)
        assertEquals(insertedMovies.size, listOfMovies.size)
    }

    @Test
    fun testInsertMovie() {
        val insertedMovies = movieDao.insert(Movie(100))
        assertEquals(insertedMovies.size, 1)
    }

    @Test
    fun deleteMovie() {
        val movie = Movie(10)
        movieDao.insert(movie)
        movieDao.delete(movie)
        movieDao.getMovie(movie.id)
            .test()
            .assertValueCount(0)
    }

    @Test
    fun testUpdateFavMovie() {
        movieDao.insert(Movie(100))
        movieDao.updateFavMovie(100, true)
        movieDao.getMovie(100)
            .test()
            .awaitCount(1)
            .assertValue {
                it.isFav
            }
    }
}
