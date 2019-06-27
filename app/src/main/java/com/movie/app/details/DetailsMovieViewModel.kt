package com.movie.app.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.modules.Movie
import com.movie.app.repositories.MovieRepository
import com.movie.app.util.LiveDataResult
import com.movie.app.util.schedulers.BaseExecutor
import kotlinx.coroutines.launch

class DetailsMovieViewModel(
    private val executor: BaseExecutor,
    private var movieRepo: MovieRepository
) : ViewModel() {

    private var movieId: Long = 0
    private var movie: Movie? = null

    private var _movieDetails: MutableLiveData<LiveDataResult<Movie>> = MutableLiveData()
    val movieDetails: LiveData<LiveDataResult<Movie>> = _movieDetails

    private var _similarMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    val similarMovies: LiveData<List<Movie>> = _similarMovies

    fun setMovieId(movieId: Long) {
        this.movieId = movieId
    }

    fun subscribe() {
        loadData()
    }

    private fun loadData() {
        _movieDetails.postValue(LiveDataResult.loading())
        viewModelScope.launch(executor.io()) {
            try {
                movieRepo.getLocalMovie(movieId)?.let {
                    movie = it
                    _movieDetails.postValue(LiveDataResult.success(movie))
                }
                movieRepo.getRemoteMovie(movieId)?.let {
                    movie = it
                    _movieDetails.postValue(LiveDataResult.success(movie))
                }
                getSimilarMovies()
            } catch (e: Exception) {
                _movieDetails.postValue(LiveDataResult.error(e))
            }
        }
    }

    private fun getSimilarMovies() {
        viewModelScope.launch(executor.io()) {
            movieRepo.getSimilarMovies(movieId)?.let {
                _similarMovies.postValue(it.results)
            }
        }
    }
}
