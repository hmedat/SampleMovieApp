package com.movie.app

class DetailsMoviePresenterTest {
/*
    private lateinit var presenter: DetailsActivityContractor.Presenter
    private lateinit var dispatcher: BaseDispatcher
    @Mock
    private lateinit var view: DetailsActivityContractor.View
    @Mock
    private lateinit var apiInterface: ApiInterface
    private lateinit var movie: Movie
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
        dispatcher = ImmediateDispatcher()
        movieRep = MovieRepository(localRep, remoteRep)
        presenter = DetailsMovieViewModel(dispatcher, movieRep, apiInterface, view)
        movie = Movie().apply {
            id = 10
            title = "Avengers 01"
        }
        presenter.setMovieId(movie.id)
    }

    @Test
    fun testSubscribeWithInternet() {
        whenever(localRep.getMovie(movie.id))
            .thenReturn(Observable.just(movie))
        whenever(remoteRep.getMovie(movie.id))
            .thenReturn(Observable.just(movie))
        whenever(apiInterface.getSimilarMoviesAsync(movie.id))
            .thenReturn(Observable.just(MoviesResult()))

        presenter.subscribe()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view, never()).showError(networkException)
        verify(view, never()).showError(diskException)
        verify(view, times(2)).showData(movie)
    }

    @Test
    fun testSubscribeWithNoInternet() {
        whenever(localRep.getMovie(movie.id))
            .thenReturn(Observable.just(movie))
        whenever(remoteRep.getMovie(movie.id))
            .thenReturn(Observable.error(networkException))

        whenever(apiInterface.getSimilarMoviesAsync(movie.id))
            .thenReturn(Observable.just(MoviesResult()))

        presenter.subscribe()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        // we don't want to show any kind of error
        verify(view, never()).showError(diskException)
        verify(view).showData(movie)
    }

    @Test
    fun testGetSimilarMovie() {
        val result = MoviesResult()
        result.page = MovieSearchFilter.First_PAGE
        result.results = listOf(Movie().apply {
            id = 1
            title = "Avengers 01"
        }, Movie().apply {
            id = 2
            title = "Avengers 02"
        }, Movie().apply {
            id = 3
            title = "Avengers 03"
        })
        whenever(apiInterface.getSimilarMoviesAsync(movie.id))
            .thenReturn(Observable.just(result))
        presenter.getSimilarMovies()

        verify(view, never()).showProgressBar()
        verify(view, never()).hideProgressBar()
        verify(view, never()).showError(IOException())
        verify(view).showSimilarMovies(result.results!!)
    }*/
}
