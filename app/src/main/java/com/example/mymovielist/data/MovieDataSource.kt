package com.example.mymovielist.data

import com.example.mymovielist.data.dto.*

interface MovieDataSource {
    suspend fun getMovies(): Result<List<MovieDTO>>
    suspend fun saveMovie(movie: MovieDTO)
    suspend fun getMovie(id: String): Result<MovieDTO>
    suspend fun deleteAllMovies()
}