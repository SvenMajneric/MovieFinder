package com.example.moviefinder.data.networking

import com.example.moviefinder.data.model.remote.Movie
import com.example.moviefinder.data.model.remote.MovieSearchApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET(".")
    suspend fun getMoviesByTitle(@Query("apikey") apiKey: String, @Query("s") movieTitle: String) : Response<MovieSearchApiResponse>

    @GET(".")
    suspend fun getMovieDetailsById(@Query("apikey") apiKey: String, @Query("i") id: String) : Response<Movie>

}