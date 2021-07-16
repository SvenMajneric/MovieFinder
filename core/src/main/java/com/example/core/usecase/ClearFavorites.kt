package com.example.core.usecase

import com.example.core.repository.MovieRepository

class ClearFavorites(private val movieRepository: MovieRepository): BaseUseCase<Int?, String> {
    override suspend fun execute(params: Int?, callback: BaseUseCase.Callback<String>) {
        return try {
            movieRepository.clearFavorites()
            callback.onSuccess("Favorites have been cleared!")
        } catch (e: Exception){
            callback.onError(e)
        }
    }
}