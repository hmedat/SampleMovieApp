package com.movie.app.room

import androidx.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDataSourceTest {

   /* private lateinit var database: AppDatabase
    private lateinit var movieDataSource: MovieDataSource
    private lateinit var genresList: List<Genre>
    private lateinit var movies: List<Movie>
    @Before
    fun setUp() {
        genresList = listOf(Genre(1).apply {
            id = 10
            name = "action"
        }, Genre(1).apply {
            id = 11
            name = "drama"
        })
        movies = listOf(Movie().apply {
            id = 10
            title = "Avengers 01"
            releaseDate = "2010"
            this.genres = genresList
            videoResult = VideoResult(Video("1001").apply {
                key = "sdfkjshd"
            }, Video("1003").apply {
                key = "sdfkjshd"
            })
        }, Movie().apply {
            id = 11
            title = "Avengers 02"
            releaseDate = "2010"
            this.genres = genresList
        })
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            AppDatabase::class.java
        ).build()
        movieDataSource = LocalMovieRepository(database)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertMovies() {
        movieDataSource.insertMovies(movies)
        assertEquals(database.movieDao().getMovies().size, movies.size)
        assertEquals(database.genreDao().getGenres().size, genresList.size)
        assertEquals(database.movieGenreDao().getAll().size, genresList.size * movies.size)
        assertEquals(database.videoDao().getVideos().size, 2)
    }

    @Test
    fun getMovies() {
        val searchFilter = MovieSearchFilter()
        searchFilter.pageNumber = 1
        val testObserver = TestObserver<MoviesResult>()
        movieDataSource.insertMovies(movies)
        movieDataSource.getMovies(searchFilter).subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
        val list = testObserver.values()[0].results
        assertEquals(list?.size, movies.size)
        assertEquals(list!![0].videosList?.size, 2)
        assertEquals(list[0].genres?.size, 2)
        assertEquals(list[1].genres?.size, 2)
    }

    @Test
    fun findMovie() {
        movieDataSource.insertMovies(movies)
        val testObserver = TestObserver<Movie>()
        movieDataSource.getMovie(11).subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
        val movie = testObserver.values()[0]
        assertNotNull(movie)
        assertEquals(movie.videosList?.size, 0)
        assertEquals(movie.genres?.size, 2)
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
        assertNull(database.movieDao().getMovie(movie.id))
    }*/
}
