package com.movie.app.room

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.movie.app.api.result.VideoResult
import com.movie.app.modules.Genre
import com.movie.app.modules.Movie
import com.movie.app.modules.Video
import com.movie.app.room.repositories.MovieRepository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var movieRepository: MovieRepository
    private lateinit var genresList: List<Genre>
    private lateinit var movies: List<Movie>
    @Before
    fun setUp() {
        genresList = listOf(Genre().apply {
            id = 10
            name = "action"
        }, Genre().apply {
            id = 11
            name = "drama"
        })
        movies = listOf(Movie().apply {
            id = 10
            title = "Avengers 01"
            releaseDate = "2010"
            this.genres = genresList
            videos = VideoResult(Video().apply {
                id = 1001
                key = "sdfkjshd"
            }, Video().apply {
                id = 1003
                key = "sdfkjshd"
            })
        }, Movie().apply {
            id = 11
            title = "Avengers 02"
            releaseDate = "2010"
            this.genres = genresList
        })
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).build()
        movieRepository = MovieRepository(database)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertMovies() {
        movieRepository.insertMovies(movies)
        Assert.assertEquals(database.movieDao().getMovies().size, movies.size)
        Assert.assertEquals(database.genreDao().getGenres().size, genresList.size)
        Assert.assertEquals(database.movieGenreDao().getAll().size, genresList.size * movies.size)
        Assert.assertEquals(database.videoDao().getVideos().size, 2)
    }

    @Test
    fun deleteMovie() {
        val movie = Movie().apply {
            id = 10
            title = "Avengers 01"
            releaseDate = "2010"
        }
        database.movieDao().insert(movie)
        database.movieDao().delete(movie)
        Assert.assertNull(database.movieDao().getMovie(movie.id))
    }
}
