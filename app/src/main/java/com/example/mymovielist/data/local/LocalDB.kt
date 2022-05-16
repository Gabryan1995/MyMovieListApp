package com.example.mymovielist.data.local

import android.content.Context
import androidx.room.Room

object LocalDB {
    /**
     * static method that creates a movie class and returns the DAO of the movie
     */
    fun createRemindersDao(context: Context): MoviesDao {
        return Room.databaseBuilder(
            context.applicationContext,
            MoviesDatabase::class.java, "myMovies.db"
        ).build().movieDao()
    }
}