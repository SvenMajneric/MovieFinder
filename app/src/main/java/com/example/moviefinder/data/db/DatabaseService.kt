package com.example.moviefinder.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviefinder.data.model.local.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class DatabaseService: RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {

        private const val DATABASE_NAME = "note.db"

        private var instance: DatabaseService? = null

        private fun create(context: Context): DatabaseService =
            Room.databaseBuilder(context, DatabaseService::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()


        fun getInstance(context: Context): DatabaseService =
            (instance ?: create(context)).also { instance = it }
    }
}