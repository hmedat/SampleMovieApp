package com.movie.app

class MovieRepositoryTest {

  /*  @Mock
    private lateinit var localRep: MovieDataSource
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

    @Test
    fun getMoviesWithNoInternetAndNoDataCached() {
        val searchFilter = MovieSearchFilter()
        searchFilter.pageNumber = 1

        whenever(localRep.getMovies(searchFilter))
            .thenReturn(Observable.error(diskException))

        whenever(remoteRep.getMovies(searchFilter))
            .thenReturn(Observable.error(networkException))

        movieRep.getMovies(searchFilter)
            .test()
            .assertValueCount(1)
            .assertError(Throwable::class.java)
        // Check if the network only deliver an error
        // Check if the db only deliver data
    }

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
