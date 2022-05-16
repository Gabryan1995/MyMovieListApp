package com.example.mymovielist.data.dto

import androidx.room.ColumnInfo
import androidx.room.TypeConverter
import com.squareup.moshi.Json
import org.json.JSONObject

class MovieResult(
    pPath: String?,
    desc: String?,
    name: String?,
    bPath: String?
) {
    @ColumnInfo(name = "poster_path")
    var posterPath: String? = pPath

    @ColumnInfo(name = "overview")
    var overview: String? = desc

    @ColumnInfo(name = "title")
    var title: String? = name

    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String? = bPath
}

class MovieResultConverter {
    @TypeConverter
    fun fromMovieResult(movie: MovieResult): String {
        return JSONObject().apply {
            put("poster_path", movie.posterPath)
            put("overview", movie.overview)
            put("title", movie.title)
            put("backdropPath", movie.backdropPath)
        }.toString()
    }

    @TypeConverter
    fun toMovieResult(movie: String): MovieResult {
        val json = JSONObject(movie)
        return MovieResult(
            json.getString("poster_path"),
            json.getString("overview"),
            json.getString("title"),
            json.getString("backdropPath")
        )
    }
}