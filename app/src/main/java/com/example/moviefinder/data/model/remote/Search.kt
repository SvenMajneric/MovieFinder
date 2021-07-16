package com.example.moviefinder.data.model.remote

import com.example.core.data.MovieBaseInfo

data class Search(
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val imdbID: String
){
    fun toMovieBaseInfo() = MovieBaseInfo(imdbID, Poster, Title, Year)
}