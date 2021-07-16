package com.example.core.usecase

import com.example.core.data.MovieBaseInfo
import com.example.core.data.MovieDetails
import com.example.core.repository.MovieRepository
import java.lang.Exception

class RemoveMovie(private val movieRepository: MovieRepository) : BaseUseCase<MovieBaseInfo, String> {
    override suspend fun execute(params: MovieBaseInfo, callback: BaseUseCase.Callback<String>) {
        return try {
            movieRepository.removeMovie(params)
            callback.onSuccess("Successfully removed from favorites!")
        } catch (e: Exception){
            callback.onError(e)
        }
    }


}