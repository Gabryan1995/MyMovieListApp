package com.example.mymovielist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mymovielist.data.dto.MovieDTO
import com.example.mymovielist.data.dto.MovieResultConverter

@Database(entities = [MovieDTO::class], version = 1, exportSchema = false)
@TypeConverters(MovieResultConverter::class)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao
}