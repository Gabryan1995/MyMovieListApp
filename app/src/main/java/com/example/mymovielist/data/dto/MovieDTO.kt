package com.example.mymovielist.data.dto

import androidx.room.*
import java.util.*

@Entity(tableName = "movies")
data class MovieDTO(
    @ColumnInfo(name = "page")
    var page: Int? = null,

    @ColumnInfo(name = "results")
    @field:TypeConverters(MovieResultConverter::class)
    var results: List<MovieResult>? = null,

    @ColumnInfo(name = "total_results")
    var totalResults: Int? = null,

    @ColumnInfo(name = "total_pages")
    var totalPages: Int? = null,

    @PrimaryKey @ColumnInfo(name = "entry_id")
    val id: String = UUID.randomUUID().toString()
)