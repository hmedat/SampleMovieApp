package com.movie.app.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.api.result.MoviesResult
import com.movie.app.repositories.MovieRepository
import com.movie.app.util.LiveDataResult
import com.movie.app.util.schedulers.BaseDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesMoviesViewModel(
    private val dispatcher: BaseDispatcher,
    private var repo: MovieRepository
) : ViewModel() {

    private var resultLiveData: MutableLiveData<LiveDataResult<MoviesResult>> = MutableLiveData()

    fun getResultLiveData(): LiveData<LiveDataResult<MoviesResult>> = resultLiveData

    fun subscribe() {
        loadData()
    }

    private fun loadData() {
        resultLiveData.postValue(LiveDataResult.loading())
        viewModelScope.launch(dispatcher.io()) {
            try {
                val movies = repo.getFavMovies()
                resultLiveData.postValue(LiveDataResult.success(movies))
            } catch (e: Exception) {
                resultLiveData.postValue(LiveDataResult.error(e))
            }
        }
    }

    fun removeFromList(movieId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeAddFavMovie(movieId, false)
        }
    }
}
