package com.example.moviefinder.presentation.moviedetails

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.data.MovieDetails
import com.example.core.usecase.BaseUseCase
import com.example.moviefinder.MovieApp
import com.example.moviefinder.data.UseCases
import com.example.moviefinder.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(application: Application, val useCases: UseCases): AndroidViewModel(application) {

    private val _notificationLiveData : MutableLiveData<Event<String>> = MutableLiveData()
    val notificationLiveData : LiveData<Event<String>> = _notificationLiveData

    private val _movieDetails: MutableLiveData<MovieDetails> = MutableLiveData()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.message.toString())
    }

    private fun onError(message: String){
        _notificationLiveData.postValue(Event(message))
    }

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

    fun fetchMovieDetailsById(id: String){
        if(hasInternetConnection()){
            viewModelScope.launch (Dispatchers.IO + exceptionHandler) {
                useCases.getMovieDetailsById.execute(id, object : BaseUseCase.Callback<MovieDetails>{
                    override fun onSuccess(result: MovieDetails) {
                        _movieDetails.postValue(result)
                    }

                    override fun onError(throwable: Throwable) {
                        _notificationLiveData.postValue(Event(throwable.message.toString()))
                    }

                })
            }
        } else {
            onError("No internet connection!")
        }
    }
}