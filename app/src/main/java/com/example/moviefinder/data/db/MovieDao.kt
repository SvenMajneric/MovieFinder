package com.example.moviefinder.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviefinder.data.model.local.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movieEntity: MovieEntity)

    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getMovieEntity(id : String) : MovieEntity?

    @Query("SELECT * FROM movie")
    fun observeAllMovieEntities() : LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movie")
    suspend fun getAllMovieEntities() : List<MovieEntity>

    @Delete
    suspend fun deleteMovieEntity(movieEntity: MovieEntity)

    @Query("DELETE FROM movie")
    suspend fun deleteAll()
}