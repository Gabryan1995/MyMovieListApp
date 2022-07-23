package com.example.mymovielist.data.dto

import com.squareup.moshi.Json

enum class MovieType { TOP_RATED, POPULAR, NOW_PLAYING }

data class MoviesPage(
    @Json(name = "results") var results: List<MovieResult> = emptyList(),
    @Json(name = "total_results") var totalResults: Int? = 0,
    @Json(name = "total_pages") var totalPages: Int? = 0,
)