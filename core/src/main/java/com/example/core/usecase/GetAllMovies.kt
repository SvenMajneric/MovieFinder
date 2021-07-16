package com.example.core.usecase

import com.example.core.data.MovieBaseInfo
import com.example.core.repository.MovieRepository

class GetAllMovies(private val movieRepository: MovieRepository) : BaseUseCase<Int?, List<MovieBaseInfo>> {
    override suspend fun execute(params: Int?, callback: BaseUseCase.Callback<List<MovieBaseInfo>>) {
        return try {
            val result = movieRepository.getAllMovies()
            callback.onSuccess(result)
        } catch (e: Exception){
            callback.onError(e)
        }
    }
}