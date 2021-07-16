package com.example.moviefinder.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.data.MovieDetails
import com.example.core.data.MovieBaseInfo

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val posterUrl: String,
    val title: String,
    val releaseDate: String,

){
    companion object{
        fun fromMovieBaseInfo(movieBaseInfo: MovieBaseInfo) = MovieEntity(movieBaseInfo.id, movieBaseInfo.poster, movieBaseInfo.title, movieBaseInfo.year)
    }

    fun toMovieBaseInfo() = MovieBaseInfo(id, posterUrl, title, releaseDate)
}
