package com.example.mymovielist.network

import com.example.mymovielist.R
import com.example.mymovielist.data.dto.MovieDTO
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"
const val IMAGE_URL = "https://image.tmdb.org/t/p/original"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface MovieApiService {
    @GET("movie/top_rated?api_key=" + R.string.moviedb_key)
    fun getTopRated(@Query("page") page: Int?): List<MovieDTO>

    @GET("movie/popular?api_key=" + R.string.moviedb_key)
    fun getPopular(@Query("page") page: Int?): List<MovieDTO>

    @GET("movie/now_playing?api_key=" + R.string.moviedb_key)
    fun getNowPlaying(@Query("page") page: Int?): List<MovieDTO>

    @GET("search/movie?api_key=" + R.string.moviedb_key)
    fun searchMovies(@Query("query") query: String, @Query("page") page: Int?): List<MovieDTO>
}

object MovieApi {
    val retrofitService : MovieApiService by lazy { retrofit.create(MovieApiService::class.java) }
}
