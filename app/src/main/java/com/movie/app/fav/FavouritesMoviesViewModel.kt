package com.movie.app.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.modules.MoviesResult
import com.movie.app.repositories.MovieRepository
import com.movie.app.util.Result
import com.movie.app.util.schedulers.BaseExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesMoviesViewModel(
    private val executor: BaseExecutor,
    private var repo: MovieRepository
) : ViewModel() {

    private var _result: MutableLiveData<Result<MoviesResult>> = MutableLiveData()
    val result: LiveData<Result<MoviesResult>> = _result

    init {
        loadData()
    }

    fun loadData() {
        _result.postValue(Result.loading())
        viewModelScope.launch(executor.io()) {
            try {
                _result.postValue(Result.success(repo.getFavMovies()))
            } catch (e: Exception) {
                _result.postValue(Result.failure(e))
            }
        }
    }

    fun removeMovie(movieId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeAddFavMovie(movieId, false)
        }
    }
}
