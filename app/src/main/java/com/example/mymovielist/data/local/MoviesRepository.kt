package com.example.mymovielist.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.mymovielist.R
import com.example.mymovielist.data.MovieDataSource
import com.example.mymovielist.data.dto.MovieResult
import com.example.mymovielist.data.dto.MovieType
import com.example.mymovielist.data.dto.MoviesPage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.mymovielist.data.dto.Result
import com.example.mymovielist.utils.MovieRemoteMediator
import kotlinx.coroutines.launch

@ExperimentalStdlibApi
class MoviesRepository(
    private val database: MoviesDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieDataSource {

    /**
     * Get the movies list from the local db
     * @return Result the holds a Success with all the movies or an Error object with the error message
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(movieType: MovieType, apiKey: String): LiveData<PagingData<MovieResult>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = MovieRemoteMediator(
                movieType,
                apiKey,
                database
            ),
            pagingSourceFactory = { database.movieDao().getMovies() }
        ).liveData
    }

    /**
     * Insert movies in the db.
     * @param movies the movies to be inserted
     */
    override suspend fun saveMovies(movies: List<MovieResult>) =
        withContext(ioDispatcher) {
            database.movieDao().saveMovies(movies)
        }

    /**
     * Deletes all the movies in the db
     */
    override suspend fun deleteAllMovies() {
        withContext(ioDispatcher) {
            database.movieDao().deleteAllMovies()
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}