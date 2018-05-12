package com.movie.app.details

import com.movie.app.api.ApiInterface
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.repositories.MovieDataSource
import com.movie.app.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DetailsMoviePresenter(private val schedulerProvider: BaseSchedulerProvider
                            , private var movieDataSource: MovieDataSource
                            , private var apiInterface: ApiInterface
                            , private val view: DetailsActivityContractor.View)
    : DetailsActivityContractor.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var movieId: Long = 0
    private var movie: Movie? = null

    override fun setMovieId(movieId: Long) {
        this.movieId = movieId
    }

    override fun subscribe() {
        loadData()
    }

    private fun loadData() {
        view.showProgressBar()
        movieDataSource.getMovie(movieId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(object : Observer<Movie> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(item: Movie) {
                        movie = item
                        view.hideProgressBar()
                        view.showData(movie!!)
                    }

                    override fun onError(throwable: Throwable) {
                        view.hideProgressBar()
                        view.showError(throwable)
                    }

                    override fun onComplete() {
                        getSimilarMovies()
                    }
                })
    }

    override fun getSimilarMovies() {
        apiInterface.getSimilarMovies(movieId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .map { it.results }
                .map {
                    MovieMapper.map(it)
                    movieDataSource.insertMovies(it)
                    it
                }
                .filter { it.isNotEmpty() }
                .subscribe(object : Observer<List<Movie>?> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(list: List<Movie>) {
                        view.showSimilarMovies(list)
                    }

                    override fun onError(throwable: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

}