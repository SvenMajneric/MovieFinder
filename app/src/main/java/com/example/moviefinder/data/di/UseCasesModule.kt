package com.example.moviefinder.data.di

import com.example.core.repository.MovieRepository
import com.example.core.usecase.*
import com.example.moviefinder.data.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {
    @Provides
    @Singleton
    fun provideUseCases(repository: MovieRepository) = UseCases(
        AddMovie(repository),
        GetAllMovies(repository),
        GetMovie(repository),
        RemoveMovie(repository),
        IfFavoriteMovieCheck(repository),
        GetMovieDetailsById(repository),
        GetMoviesByTitle(repository),
        ClearFavorites(repository)
    )
}