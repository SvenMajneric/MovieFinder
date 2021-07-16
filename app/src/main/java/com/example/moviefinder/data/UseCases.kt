package com.example.moviefinder.data

import com.example.core.usecase.*

data class UseCases (
    val addMovie: AddMovie,
    val getAllMovies: GetAllMovies,
    val getMovie: GetMovie,
    val removeMovie: RemoveMovie,
    val ifFavoriteMovieCheck: IfFavoriteMovieCheck,
    val getMovieDetailsById: GetMovieDetailsById,
    val getMoviesByTitle: GetMoviesByTitle,
    val clearFavorites: ClearFavorites
        )