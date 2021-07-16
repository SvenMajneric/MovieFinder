package com.example.moviefinder.presentation.search

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.data.MovieBaseInfo
import com.example.core.usecase.BaseUseCase
import com.example.moviefinder.MovieApp
import com.example.moviefinder.data.UseCases
import com.example.moviefinder.util.Event
import com.example.moviefinder.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(application: Application, val useCases: UseCases): AndroidViewModel(application) {

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<MovieApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


    private val _notificationLiveData : MutableLiveData<Event<String>> = MutableLiveData()
    val notificationLiveData : LiveData<Event<String>> = _notificationLiveData

    private val _movieListGroupedByYear : MutableLiveData<Resource<Map<String, List<MovieBaseInfo>>>> = MutableLiveData()
    val movieListGroupedByYear: LiveData<Resource<Map<String, List<MovieBaseInfo>>>> = _movieListGroupedByYear

    private val _favorites : MutableLiveData<Resource<List<MovieBaseInfo>>> = MutableLiveData()
    val favorites : LiveData<Resource<List<MovieBaseInfo>>> = _favorites

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            onError(throwable.message.toString())
    }

    private fun onError(message: String){
        _notificationLiveData.postValue(Event(message))
    }


    fun fetchMoviesFromApi(title: String){
        if (hasInternetConnection()) {
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                _movieListGroupedByYear.postValue(Resource.Loading())

                useCases.getMoviesByTitle.execute(title,
                    object : BaseUseCase.Callback<Map<String, List<MovieBaseInfo>>> {

                        override fun onSuccess(result: Map<String, List<MovieBaseInfo>>) {

                            _movieListGroupedByYear.postValue(Resource.Success(result))
                        }

                        override fun onError(throwable: Throwable) {
                            onError("This movie name returns no results. \n Please try another one!")
                        }

                    })

            }
        } else {
            onError("No internet connection.")
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




    fun addMovieToFavorites(movie: MovieBaseInfo){
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

                useCases.addMovie.execute(movie, object : BaseUseCase.Callback<String>{
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