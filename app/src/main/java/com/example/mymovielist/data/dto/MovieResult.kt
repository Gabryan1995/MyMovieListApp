package com.example.mymovielist.data.dto

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movies")
data class MovieResult(
    @ColumnInfo(name = "poster_path")
    @Json(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "overview")
    val overview: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "backdrop_path")
    @Json(name = "backdrop_path")
    val backdropPath: String?,

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int
): Parcelable