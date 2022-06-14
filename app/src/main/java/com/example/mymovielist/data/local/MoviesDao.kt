package com.example.mymovielist.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymovielist.data.dto.MoviesPage
import com.example.mymovielist.data.dto.MovieResult

@ExperimentalStdlibApi
@Dao
interface MoviesDao {
    /**
     * @return all movies.
     */
    @Query("SELECT * FROM movies")
    fun getMovies(): PagingSource<Int, MovieResult>

    /**
     * Insert movies in the database. If the movies already exist, replace them.
     *
     * @param movies the movies to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movies: List<MovieResult>)

    /**
     * Delete all movies.
     */
    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}