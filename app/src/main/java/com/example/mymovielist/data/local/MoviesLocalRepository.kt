package com.example.mymovielist.data.local

import com.example.mymovielist.data.MovieDataSource
import com.example.mymovielist.data.dto.MovieDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.mymovielist.data.dto.Result

class MoviesLocalRepository(
    private val moviesDao: MoviesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieDataSource {

    /**
     * Get the movies list from the local db
     * @return Result the holds a Success with all the movies or an Error object with the error message
     */
    override suspend fun getMovies(): Result<List<MovieDTO>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(moviesDao.getMovies())
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    /**
     * Insert a movie in the db.
     * @param movie the movie to be inserted
     */
    override suspend fun saveMovie(movie: MovieDTO) =
        withContext(ioDispatcher) {
            moviesDao.saveMovie(movie)
        }

    /**
     * Get a movie by its id
     * @param id to be used to get the movie
     * @return Result the holds a Success object with the Movie or an Error object with the error message
     */
    override suspend fun getMovie(id: String): Result<MovieDTO> = withContext(ioDispatcher) {
        try {
            val movie = moviesDao.getMovieById(id)
            if (movie != null) {
                return@withContext Result.Success(movie)
            } else {
                return@withContext Result.Error("Movie not found!")
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    /**
     * Deletes all the movies in the db
     */
    override suspend fun deleteAllMovies() {
        withContext(ioDispatcher) {
            moviesDao.deleteAllMovies()
        }
    }
}