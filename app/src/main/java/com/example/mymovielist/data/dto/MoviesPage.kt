package com.example.mymovielist.data.dto

import com.squareup.moshi.Json

enum class MovieType { TOP_RATED, POPULAR, NOW_PLAYING }

data class MoviesPage(
    var nextPage: Int? = null,
    @Json(name = "results") var results: List<MovieResult> = emptyList(),
    @Json(name = "total_results") var totalResults: Int? = null,
    @Json(name = "total_pages") var totalPages: Int? = null,
    var movieType: MovieType = MovieType.TOP_RATED
)