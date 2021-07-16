package com.example.core.usecase

import com.example.core.data.MovieBaseInfo
import com.example.core.data.MovieDetails
import com.example.core.repository.MovieRepository

class AddMovie(private val movieRepository: MovieRepository): BaseUseCase<MovieBaseInfo, String> {
    override suspend fun execute(params: MovieBaseInfo, callback: BaseUseCase.Callback<String>) {
        return try {
            movieRepository.addMovie(params)
            callback.onSuccess("Movie successfully added to favorites!")
        } catch (e : Exception){
            callback.onError(e)
        }
    }
}