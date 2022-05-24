package com.example.mymovielist.data.dto

import android.os.Parcelable
import androidx.room.TypeConverter
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.parcelize.Parcelize

@Parcelize
class MovieResult(
    @Json(name = "poster_path")
    val posterPath: String?,
    val overview: String?,
    val title: String?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    val id: Int?
    ) : Parcelable

class MovieResults(_movies: List<MovieResult>?) {
    var movies: List<MovieResult>? = _movies
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

        return MovieResults(movies?.movies)
    }
}