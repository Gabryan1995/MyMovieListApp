package com.example.mymovielist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymovielist.data.dto.MovieDTO

@Database(entities = [MovieDTO::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao
}