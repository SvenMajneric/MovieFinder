package com.example.core.repository

import com.example.core.data.MovieBaseInfo
import com.example.core.data.MovieDetails

interface MovieDataSource {

    suspend fun getMoviesByTitle(title: String) : List<MovieBaseInfo>

    suspend fun getMovieDetailsById(id: String) : MovieDetails

    suspend fun isFavorite(imdbId: String) : Boolean

    suspend fun add(movieBaseInfo: MovieBaseInfo)

    suspend fun get(imdbId: String): MovieBaseInfo

    suspend fun getAll(): List<MovieBaseInfo>

    suspend fun remove(movie: MovieBaseInfo)

    suspend fun clearFavorites()
}