package com.example.mymovielist.data.dto

import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@ExperimentalStdlibApi
@JsonClass(generateAdapter = true)
@TypeConverters(MovieResultsConverter::class)
@Entity(tableName = "movies")
data class MovieDTO(
    @ColumnInfo(name = "page")
    @Json(name = "page")
    var page: Int? = null,

    @ColumnInfo(name = "results")
    @Json(name = "results")
    var results: MovieResults? = null,

    @ColumnInfo(name = "total_results")
    @Json(name = "total_results")
    var totalResults: Int? = null,

    @ColumnInfo(name = "total_pages")
    @Json(name = "total_pages")
    var totalPages: Int? = null,

    @PrimaryKey @ColumnInfo(name = "entry_id")
    val id: String = UUID.randomUUID().toString()
)