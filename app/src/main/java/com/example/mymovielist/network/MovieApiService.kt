package com.example.mymovielist.network

import com.example.mymovielist.data.dto.MoviesPage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"
const val IMAGE_URL = "https://image.tmdb.org/t/p/original"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

@ExperimentalStdlibApi
interface MovieApiService {
    @GET("movie/{category}")
    suspend fun getMovies(
        @Path("category") category: String,
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int?
    ): MoviesPage

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String?,
        @Query("query") query: String,
        @Query("page") page: Int?
    ): MoviesPage
}

@ExperimentalStdlibApi
object MovieApi {
    val retrofitService : MovieApiService by lazy { retrofit.create(MovieApiService::class.java) }
}
