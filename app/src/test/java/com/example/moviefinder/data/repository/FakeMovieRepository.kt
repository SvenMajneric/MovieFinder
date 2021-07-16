package com.example.moviefinder.data.repository

import com.example.core.data.MovieBaseInfo
import com.example.core.repository.MovieDataSource

class FakeMovieRepository(private val dataSource: MovieDataSource) {
    suspend fun addMovie(movie: MovieBaseInfo) = dataSource.add(movie)

    suspend fun getMovie(imdbId: String) = dataSource.get(imdbId)

    suspend fun getAllMovies() = dataSource.getAll()

    suspend fun removeMovie(movie: MovieBaseInfo) = dataSource.remove(movie)

    suspend fun isFavorite(imdbId: String) = dataSource.isFavorite(imdbId)

    suspend fun getMovieDetailsById(id: String) = dataSource.getMovieDetailsById(id)

    suspend fun getMoviesByTitle(title: String) = dataSource.getMoviesByTitle(title)

    suspend fun clearFavorites() = dataSource.clearFavorites()
}