package com.movie.app.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.movie.app.interactors.IMoviesInteractor
import com.movie.app.modules.Movie
import com.movie.app.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DetailsMoviePresenter(private val schedulerProvider: BaseSchedulerProvider
                            , private var moviesInteractor: IMoviesInteractor
                            , private val view: DetailsActivityContractor.View)
    : DetailsActivityContractor.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var movie: Movie

    override fun setExtraBundles(extras: Bundle) {
        movie = extras.getParcelable(DetailsMovieActivity.EXTRA_MOVIE)
    }

    override fun subscribe() {
        loadData()
    }

    private fun loadData() {
        view.showProgressBar()
        moviesInteractor.findMovie(movieId = movie.id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(object : Observer<Movie> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(item: Movie) {
                        movie = item
                        view.hideProgressBar()
                        view.showData(movie)
                    }

                    override fun onError(throwable: Throwable) {
                        view.hideProgressBar()
                        view.showError(throwable)
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun showTrailerVideo() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.firstVideoUrl))
        val chooser: Intent = Intent.createChooser(intent, "")
        view.startYoutubeActivity(chooser)
    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

}