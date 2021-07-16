package com.example.core.repository

import com.example.core.data.MovieBaseInfo

class MovieRepository(private val dataSource: MovieDataSource) {
    suspend fun addMovie(movie: MovieBaseInfo) = dataSource.add(movie)

    suspend fun getMovieFromLocalSource(imdbId: String) = dataSource.get(imdbId)

    suspend fun getAllMovies() = dataSource.getAll()

    suspend fun removeMovie(movie: MovieBaseInfo) = dataSource.remove(movie)

    suspend fun isFavorite(imdbId: String) = dataSource.isFavorite(imdbId)

    suspend fun getMovieDetailsById(id: String) = dataSource.getMovieDetailsById(id)

    suspend fun getMoviesByTitle(title: String) = dataSource.getMoviesByTitle(title)

    suspend fun clearFavorites() = dataSource.clearFavorites()
}