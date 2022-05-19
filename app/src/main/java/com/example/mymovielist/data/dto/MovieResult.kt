package com.example.mymovielist.data.dto

import androidx.room.ColumnInfo
import androidx.room.TypeConverter
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MovieResult(
    pPath: String?,
    desc: String?,
    name: String?,
    bPath: String?
) {
    @ColumnInfo(name = "poster_path")
    @Json(name = "poster_path")
    var posterPath: String? = pPath

    @ColumnInfo(name = "overview")
    @Json(name = "overview")
    var overview: String? = desc

    @ColumnInfo(name = "title")
    @Json(name = "title")
    var title: String? = name

    @ColumnInfo(name = "backdrop_path")
    @Json(name = "backdrop_path")
    var backdropPath: String? = bPath
}

class MovieResults(_movies: MovieResults?) {
    var movies: List<MovieResult>? = _movies?.movies
}

@ExperimentalStdlibApi
class MovieResultsConverter {
    @TypeConverter
    fun fromMovieResults(movies: MovieResults): String {
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return moshi.adapter<MovieResults>().toJson(movies)
    }

    @TypeConverter
    fun toMovieResults(moviesString: String): MovieResults {
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val movies = moshi.adapter<MovieResults>().fromJson(moviesString)

        return MovieResults(movies)
    }
}