package com.example.mymovielist.data.dto

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "movies")
@Parcelize
data class MovieDTO(
    @Json(name = "page") var page: Int?,
    var title: String?,
    @Json(name = "overview") var description: String?,
    @Json(name = "poster_path") var posterPath: String?,
    @Json(name = "backdrop_path") var backdropPath: String?,
    @PrimaryKey @ColumnInfo(name = "entry_id") val id: String = UUID.randomUUID().toString()
) : Parcelable

