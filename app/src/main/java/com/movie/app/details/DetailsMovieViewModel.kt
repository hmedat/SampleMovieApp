package com.movie.app.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.api.ApiInterface
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.repositories.MovieRepository
import com.movie.app.util.LiveDataResult
import com.movie.app.util.schedulers.BaseDispatcher
import kotlinx.coroutines.launch

class DetailsMovieViewModel(
    private val dispatcher: BaseDispatcher,
    private var movieRepo: MovieRepository,
    private var apiInterface: ApiInterface
) : ViewModel() {

    private var movieId: Long = 0
    private var movie: Movie? = null

    private var _movieDetails: MutableLiveData<LiveDataResult<Movie>> = MutableLiveData()
    fun getMovieDetailsLiveData(): LiveData<LiveDataResult<Movie>> = _movieDetails

    private var _similarMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    fun getSimilarMoviesLiveData(): LiveData<List<Movie>> = _similarMovies

    fun setMovieId(movieId: Long) {
        this.movieId = movieId
    }

    fun subscribe() {
        loadData()
    }

    private fun loadData() {
        _movieDetails.postValue(LiveDataResult.loading())
        viewModelScope.launch(dispatcher.io()) {
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
        viewModelScope.launch(dispatcher.io()) {
            val result = apiInterface.getSimilarMoviesAsync(movieId).await().body()?.results
            if (result.isNullOrEmpty()) {
                return@launch
            }
            movieRepo.insertMovies(result)
            MovieMapper.map(result)
            _similarMovies.postValue(result)
        }
    }
}
