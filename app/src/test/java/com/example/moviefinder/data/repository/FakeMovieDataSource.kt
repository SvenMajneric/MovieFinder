package com.example.moviefinder.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.core.data.MovieBaseInfo
import com.example.core.data.MovieDetails
import com.example.core.repository.MovieDataSource

class FakeMovieDataSource : MovieDataSource {

    private val favoriteMovies = mutableListOf<MovieBaseInfo>()

    private val observableFavoriteMovies = MutableLiveData<List<MovieBaseInfo>>(favoriteMovies)

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData(){
        observableFavoriteMovies.postValue(favoriteMovies)
    }

    fun observeAllMovieEntities() : LiveData<List<MovieBaseInfo>>{
        return observableFavoriteMovies
    }

    override suspend fun getMoviesByTitle(title: String): List<MovieBaseInfo> {
        return if(shouldReturnNetworkError){
             listOf(MovieBaseInfo("networkError", "http", "error occurred", "now"))
        } else {
            listOf(MovieBaseInfo("id", "poster", title, "2000"))

        }
    }

    override suspend fun getMovieDetailsById(id: String): MovieDetails {
        return if(shouldReturnNetworkError){
            MovieDetails(
                "networkError",
                "http",
                "error occurred",
                "now",
                "1hr",
                "10",
                "horror",
                "error"
            )
        } else {
            MovieDetails(
                id,
                "poster",
                "title",
                "2000",
                "1hr",
                "10",
                "horror",
                "movie"
            )
        }
    }

    override suspend fun isFavorite(imdbId: String): Boolean {
        for (movie in favoriteMovies) {
            if (movie.id == imdbId) return true
        }
        return false
    }

    override suspend fun add(movieBaseInfo: MovieBaseInfo) {
        favoriteMovies.add(movieBaseInfo)
        refreshLiveData()
    }

    override suspend fun get(imdbId: String): MovieBaseInfo {
        for (movie in favoriteMovies) {
            if (movie.id == imdbId) return movie
        }
        return MovieBaseInfo("networkError", "http", "error occurred", "now")
    }

    override suspend fun getAll(): List<MovieBaseInfo> {
        return favoriteMovies
    }

    override suspend fun remove(movie: MovieBaseInfo) {
        if (movie in favoriteMovies) {
            favoriteMovies.remove(movie)
            refreshLiveData()
        }
    }

    override suspend fun clearFavorites() {
        favoriteMovies.clear()
    }
}