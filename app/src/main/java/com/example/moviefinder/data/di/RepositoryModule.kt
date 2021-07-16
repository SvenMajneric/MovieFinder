package com.example.moviefinder.data.di

import android.content.Context
import com.example.core.repository.MovieRepository
import com.example.moviefinder.data.db.MovieDao
import com.example.moviefinder.data.networking.MovieService
import com.example.moviefinder.data.repository.DefaultMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(movieDao: MovieDao, apiService: MovieService) = MovieRepository(DefaultMovieRepository(apiService, movieDao))
}