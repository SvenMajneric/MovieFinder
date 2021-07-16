package com.example.core.usecase

import com.example.core.data.MovieBaseInfo
import com.example.core.repository.MovieRepository

class GetMovie(private val movieRepository: MovieRepository) : BaseUseCase<String, MovieBaseInfo> {
    override suspend fun execute(params: String, callback: BaseUseCase.Callback<MovieBaseInfo>) {
        return try {
            val result = movieRepository.getMovieFromLocalSource(params)
            callback.onSuccess(result)
        } catch (e: Exception){
            callback.onError(e)
        }
    }
}