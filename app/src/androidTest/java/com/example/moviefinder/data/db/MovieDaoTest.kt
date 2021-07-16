package com.example.moviefinder.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.moviefinder.data.model.local.MovieEntity
import com.example.moviefinder.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DatabaseService
    private lateinit var dao: MovieDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DatabaseService::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.movieDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun saveMovie() = runBlockingTest {
        val movie = MovieEntity("myId", "https://youtube.com", "Mumija Parodija", "2004")
        dao.addMovie(movie)

        val allFavoriteMovies = dao.observeAllMovieEntities().getOrAwaitValue()
        assertThat(allFavoriteMovies).contains(movie)
    }

    @Test
    fun deleteMovie() = runBlockingTest {
        val movie = MovieEntity("myId", "https://youtube.com", "Mumija Parodija", "2004")
        dao.addMovie(movie)
        dao.deleteMovieEntity(movie)

        val allFavoriteMovies = dao.observeAllMovieEntities().getOrAwaitValue()

        assertThat(allFavoriteMovies).doesNotContain(movie)
    }

    @Test
    fun getSpecificMovie() = runBlockingTest {
        val movie = MovieEntity("myId", "https://youtube.com", "Mumija Parodija", "2004")
        dao.addMovie(movie)
        val movieFromDatabase = dao.getMovieEntity(movie.id)

        assertThat(movie).isEqualTo(movieFromDatabase)
    }
}