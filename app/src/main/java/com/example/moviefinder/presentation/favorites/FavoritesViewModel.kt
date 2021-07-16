package com.example.moviefinder.presentation.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.data.MovieBaseInfo
import com.example.core.usecase.BaseUseCase
import com.example.moviefinder.data.UseCases
import com.example.moviefinder.util.Event
import com.example.moviefinder.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(application: Application, val useCases: UseCases): AndroidViewModel(application) {


    private val _notificationLiveData : MutableLiveData<Event<String>> = MutableLiveData()
    val notificationLiveData : LiveData<Event<String>> = _notificationLiveData

    private val _favorites : MutableLiveData<Resource<List<MovieBaseInfo>>> = MutableLiveData()
    val favorites : LiveData<Resource<List<MovieBaseInfo>>> = _favorites

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.message.toString())
    }

    private fun onError(message: String){
        _notificationLiveData.postValue(Event(message))
    }



    fun clearFavorites(){
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            useCases.clearFavorites.execute(null, object: BaseUseCase.Callback<String>{
                override fun onSuccess(result: String) {
                    _notificationLiveData.postValue(Event(result))
                }

                override fun onError(throwable: Throwable) {
                    _notificationLiveData.postValue(Event(throwable.message.toString()))
                }

            })
            getAllFavoriteMovies()
        }
    }


    fun getAllFavoriteMovies() =
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _favorites.postValue(Resource.Loading())
            useCases.getAllMovies.execute(null, object : BaseUseCase.Callback<List<MovieBaseInfo>> {
                override fun onSuccess(result: List<MovieBaseInfo>) {
                    _favorites.postValue(Resource.Success(result))
                }

                override fun onError(throwable: Throwable) {
                    _favorites.postValue(Resource.Error(throwable.message.toString()))
                }
            })
        }





    fun removeMovieFromFavorites(movieBaseInfo : MovieBaseInfo){
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            useCases.removeMovie.execute(movieBaseInfo, object : BaseUseCase.Callback<String>{
                override fun onSuccess(result: String) {
                    _notificationLiveData.postValue(Event(result))
                }

                override fun onError(throwable: Throwable) {
                    _notificationLiveData.postValue(Event(throwable.message.toString()))
                }
            })
            getAllFavoriteMovies()
        }

    }
}