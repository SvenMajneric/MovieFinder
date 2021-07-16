package com.example.moviefinder.data.di

import android.content.Context
import androidx.room.Room
import com.example.moviefinder.data.db.DatabaseService
import com.example.moviefinder.data.db.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DatabaseService{
        return Room
            .databaseBuilder(context, DatabaseService::class.java, "MovieApp")
            .build()
    }


    @Provides
    fun provideDao(database: DatabaseService): MovieDao{
        return database.movieDao()
    }
}