package com.example.core.usecase

import com.example.core.data.MovieDetails
import com.example.core.repository.MovieRepository

class GetMovieDetailsById(private val movieRepository: MovieRepository) : BaseUseCase<String, MovieDetails> {

    override suspend fun execute(params: String, callback: BaseUseCase.Callback<MovieDetails>) {
        return try {
            val movieDetails = movieRepository.getMovieDetailsById(params)
            callback.onSuccess(movieDetails)

        } catch (e: Exception){
            callback.onError(e)
        }


    }
}
