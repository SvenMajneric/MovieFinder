package com.example.moviefinder.data.model.remote

data class MovieSearchApiResponse(
    val Response: String,
    val Search: List<Search>,
    val totalResults: String
)