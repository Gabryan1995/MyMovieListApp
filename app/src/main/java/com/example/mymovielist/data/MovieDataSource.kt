package com.example.mymovielist.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.mymovielist.data.dto.*

@ExperimentalStdlibApi
interface MovieDataSource {
    fun getMovies(apiKey: String): Result<LiveData<PagingData<MovieResult>>>
    suspend fun saveMovies(movies: List<MovieResult>)
    suspend fun deleteAllMovies()
}