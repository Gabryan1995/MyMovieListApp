package com.example.mymovielist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymovielist.data.dto.MovieDTO

@ExperimentalStdlibApi
@Dao
interface MoviesDao {
    /**
     * @return all movies.
     */
    @Query("SELECT * FROM movies")
    suspend fun getMovies(): List<MovieDTO>

    /**
     * @param movieId the id of the movie
     * @return the movie object with the movieId
     */
    @Query("SELECT * FROM movies where entry_id = :movieId")
    suspend fun getMovieById(movieId: String): MovieDTO?

    /**
     * Insert a movie in the database. If the movie already exists, replace it.
     *
     * @param movie the movie to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(reminder: MovieDTO)

    /**
     * Delete all movies.
     */
    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

}