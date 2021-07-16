package com.example.moviefinder.data.repository


import com.example.core.data.MovieBaseInfo
import com.example.core.data.MovieDetails
import com.example.core.repository.MovieDataSource
import com.example.moviefinder.BuildConfig.API_KEY
import com.example.moviefinder.data.db.MovieDao
import com.example.moviefinder.data.model.local.MovieEntity
import com.example.moviefinder.data.networking.MovieService

import javax.inject.Inject

class DefaultMovieRepository@Inject constructor(
    private val api: MovieService,
    private val movieDao: MovieDao
): MovieDataSource {

    override suspend fun getMoviesByTitle(title: String): List<MovieBaseInfo> = api.getMoviesByTitle(API_KEY, title).body()!!.Search.map { it.toMovieBaseInfo() }


    override suspend fun getMovieDetailsById(id: String): MovieDetails = api.getMovieDetailsById(API_KEY, id).body()!!.toMovieDetails()

    override suspend fun isFavorite(imdbId: String): Boolean {
        val getMovieByImdbId = movieDao.getMovieEntity(imdbId)
        return getMovieByImdbId != null
    }

    override suspend fun add(movieBaseInfo: MovieBaseInfo) = movieDao.addMovie(MovieEntity.fromMovieBaseInfo(movieBaseInfo))

    override suspend fun get(imdbId: String): MovieBaseInfo = movieDao.getMovieEntity(imdbId)!!.toMovieBaseInfo()

    override suspend fun getAll(): List<MovieBaseInfo> = movieDao.getAllMovieEntities().map { it.toMovieBaseInfo() }

    override suspend fun remove(movie: MovieBaseInfo) = movieDao.deleteMovieEntity(MovieEntity.fromMovieBaseInfo(movie))

    override suspend fun clearFavorites() = movieDao.deleteAll()


}