package com.movie.app.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.api.result.MoviesResult
import com.movie.app.repositories.MovieRepository
import com.movie.app.util.LiveDataResult
import com.movie.app.util.schedulers.BaseExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesMoviesViewModel(
    private val executor: BaseExecutor,
    private var repo: MovieRepository
) : ViewModel() {

    private var _result: MutableLiveData<LiveDataResult<MoviesResult>> = MutableLiveData()

    fun getResultLiveData(): LiveData<LiveDataResult<MoviesResult>> = _result

    fun subscribe() {
        loadData()
    }

    private fun loadData() {
        _result.postValue(LiveDataResult.loading())
        viewModelScope.launch(executor.io()) {
            try {
                val movies = repo.getFavMovies()
                _result.postValue(LiveDataResult.success(movies))
            } catch (e: Exception) {
                _result.postValue(LiveDataResult.error(e))
            }
        }
    }

    fun removeFromList(movieId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeAddFavMovie(movieId, false)
        }
    }
}
