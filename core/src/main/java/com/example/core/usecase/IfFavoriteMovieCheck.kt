package com.example.core.usecase

import com.example.core.repository.MovieRepository

class IfFavoriteMovieCheck(private val movieRepository: MovieRepository) : BaseUseCase<String, Boolean> {
    override suspend fun execute(params: String, callback: BaseUseCase.Callback<Boolean>) {
        return try {
             val result = movieRepository.isFavorite(params)
            callback.onSuccess(result)
        } catch (e: Exception){
            callback.onError(e)
        }
    }
}