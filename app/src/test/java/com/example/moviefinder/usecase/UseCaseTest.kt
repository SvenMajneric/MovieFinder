package com.example.moviefinder.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core.data.MovieBaseInfo
import com.example.core.data.MovieDetails
import com.example.moviefinder.data.repository.FakeMovieDataSource
import com.example.moviefinder.data.repository.FakeMovieRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class UseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dataSource: FakeMovieDataSource
    private lateinit var repository: FakeMovieRepository
    private lateinit var movie: MovieBaseInfo
    private lateinit var movieBaseInfoError: MovieBaseInfo
    private lateinit var movieDetailsError: MovieDetails

    @Before
    fun setup(){
        movie = MovieBaseInfo("id", "poster", "title", "2000")
        movieBaseInfoError = MovieBaseInfo("networkError", "http", "error occurred", "now")
        movieDetailsError = MovieDetails(
            "networkError",
            "http",
            "error occurred",
            "now",
            "1hr",
            "10",
            "horror",
            "error"
        )
        dataSource = FakeMovieDataSource()
        repository = FakeMovieRepository(dataSource)
    }

    @Test
    fun `use case adds movie to list of movies`()= runBlockingTest {
        repository.addMovie(movie)

        val listOfMovies = repository.getAllMovies()

        assertThat(listOfMovies).contains(movie)
    }

    @Test
    fun `use case deletes movie from list`() = runBlockingTest {
        repository.addMovie(movie)
        repository.removeMovie(movie)

        val listOfMovies = repository.getAllMovies()

        assertThat(listOfMovies).doesNotContain(movie)
    }

    @Test
    fun `use case checks if movie is in the list`()= runBlockingTest {
        repository.addMovie(movie)

        val movieIsFavorite = repository.isFavorite(movie.id)

        assertThat(movieIsFavorite).isTrue()
    }

    @Test
    fun `use case gets movies by title`() = runBlockingTest {
        val fakeApiResponse = repository.getMoviesByTitle(movie.title)

        assertThat(fakeApiResponse).contains(movie)
    }

    @Test
    fun `network error while fetching movies returns error`() = runBlockingTest {
        dataSource.setShouldReturnNetworkError(true)

        val fakeApiResponse = repository.getMoviesByTitle(movie.title)

        assertThat(fakeApiResponse).contains(movieBaseInfoError)
    }

    @Test
    fun `use case gets movie details by id`() = runBlockingTest {
        val fakeApiResponse = repository.getMovieDetailsById(movie.id)

        assertThat(fakeApiResponse).isEqualTo(MovieDetails(
            "id",
            "poster",
            "title",
            "2000",
            "1hr",
            "10",
            "horror",
            "movie"
        ))
    }

    @Test
    fun `network error while fetching movie details returns error`() = runBlockingTest {
        dataSource.setShouldReturnNetworkError(true)

        val fakeApiResponse = repository.getMovieDetailsById(movie.id)

        assertThat(fakeApiResponse).isEqualTo(movieDetailsError)
    }


    @Test
    fun `clear function empties movie list`() = runBlockingTest {
        dataSource.add(movie)
        dataSource.add(movie)
        dataSource.add(movie)

        dataSource.clearFavorites()

        val movies = dataSource.getAll()

        assertThat(movies).isEmpty()
    }
}