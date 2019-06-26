package com.movie.app.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.api.result.MoviesResult
import com.movie.app.repositories.MovieDataSource
import com.movie.app.util.LiveDataResult
import com.movie.app.util.schedulers.BaseSchedulerProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesMoviesViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private var movieDataSource: MovieDataSource
) : ViewModel() {

    private var resultLiveData: MutableLiveData<LiveDataResult<MoviesResult>> = MutableLiveData()

    fun getResultLiveData(): LiveData<LiveDataResult<MoviesResult>> = resultLiveData

    fun subscribe() {
        loadData()
    }

    private fun loadData() {
        resultLiveData.postValue(LiveDataResult.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = movieDataSource.getFavMovies()
                resultLiveData.postValue(LiveDataResult.success(movies))
            } catch (e: Exception) {
                resultLiveData.postValue(LiveDataResult.error(e))
            }
        }
    }

    fun removeFromList(movieId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDataSource.removeAddFavMovie(movieId, false)
        }
    }
}
