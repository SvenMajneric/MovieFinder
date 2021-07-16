package com.example.core.usecase

import com.example.core.data.MovieBaseInfo
import com.example.core.repository.MovieRepository

class GetMoviesByTitle(private val movieRepository: MovieRepository) : BaseUseCase<String, Map<String, List<MovieBaseInfo>>>{

    override suspend fun execute(params: String, callback: BaseUseCase.Callback<Map<String, List<MovieBaseInfo>>>) {
        return try {
            val movieList = movieRepository.getMoviesByTitle(params)
            callback.onSuccess(movieList.sortedBy { it.year }.groupBy { it.year })
        } catch (e: Exception){
            callback.onError(e)
        }
    }
}