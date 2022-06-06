package com.example.mymovielist.data.local

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mymovielist.data.MovieDataSource
import com.example.mymovielist.data.dto.MovieResult
import com.example.mymovielist.data.dto.MoviesPage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.mymovielist.data.dto.Result
import com.example.mymovielist.utils.MovieRemoteMediator

@ExperimentalPagingApi
@ExperimentalStdlibApi
class MoviesLocalRepository(
    private val moviesDao: MoviesDao,
    private val database: MoviesDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieDataSource {

    /**
     * Get the movies list from the local db
     * @return Result the holds a Success with all the movies or an Error object with the error message
     */
    override suspend fun getMovies(apiKey: String): Result<LiveData<PagingData<MovieResult>>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(
                Pager(
                    config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
                    remoteMediator = MovieRemoteMediator(
                        apiKey,
                        database
                    ),
                    pagingSourceFactory = moviesDao.getMovies()
                ).liveData
            )
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    /**
     * Insert movies in the db.
     * @param movies the movies to be inserted
     */
    override suspend fun saveMovies(movies: MoviesPage) =
        withContext(ioDispatcher) {
            moviesDao.saveMovies(movies)
        }

    /**
     * Deletes all the movies in the db
     */
    override suspend fun deleteAllMovies() {
        withContext(ioDispatcher) {
            moviesDao.deleteAllMovies()
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}