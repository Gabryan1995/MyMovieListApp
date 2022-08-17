package com.example.mymovielist.ui.browse

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.mymovielist.R
import com.example.mymovielist.base.BaseViewModel
import com.example.mymovielist.data.MovieDataSource
import com.example.mymovielist.data.dto.*
import com.example.mymovielist.data.local.MoviesDatabase
import com.example.mymovielist.utils.MovieRemoteMediator
import kotlinx.coroutines.launch
import kotlin.Result.*

enum class MoviesApiStatus { LOADING, ERROR, DONE }

@ExperimentalPagingApi
@ExperimentalStdlibApi
class BrowseViewModel(
    app: Application,
    private val dataSource: MovieDataSource,
    private val savedStateHandle: SavedStateHandle
    ): BaseViewModel(app) {

    private val _status = MutableLiveData<MoviesApiStatus>()
    val status: LiveData<MoviesApiStatus>
        get() = _status

    private var _topRatedMovies = MutableLiveData<PagingData<MovieResult>>()
    val topRatedMovies: LiveData<PagingData<MovieResult>>
        get() = _topRatedMovies

    private var _popularMovies = MutableLiveData<PagingData<MovieResult>>()
    val popularMovies: LiveData<PagingData<MovieResult>>
        get() = _popularMovies

    private var _nowPlayingMovies = MutableLiveData<PagingData<MovieResult>>()
    val nowPlayingMovies: LiveData<PagingData<MovieResult>>
        get() = _nowPlayingMovies

    private val _navigateToSelectedMovie = MutableLiveData<MovieResult?>()
    val navigateToSelectedMovie: LiveData<MovieResult?>
        get() = _navigateToSelectedMovie

    private val database = MoviesDatabase.getInstance(app)

    init {
        loadMovies()
    }

    private fun loadMovies() {
        showLoading.value = true

        viewModelScope.launch {
            //interacting with the dataSource has to be through a coroutine
            val topRatedResult = dataSource.getMovies(
                MovieType.TOP_RATED,
                getApplication<Application>().applicationContext.getString(R.string.moviedb_key)
            )
            val popularResult = dataSource.getMovies(
                MovieType.POPULAR,
                getApplication<Application>().applicationContext.getString(R.string.moviedb_key)
            )
            val nowPlayingResult = dataSource.getMovies(
                MovieType.NOW_PLAYING,
                getApplication<Application>().applicationContext.getString(R.string.moviedb_key)
            )
            showLoading.postValue(false)
            when (topRatedResult) {
                is Result.Success<*> -> {
                    val dataList = (topRatedResult.data as LiveData<PagingData<MovieResult>>).value?.map { movie ->
                        //map the reminder data from the DB to the be ready to be displayed on the UI
                        MovieResult(
                            movie.posterPath,
                            movie.overview,
                            movie.title,
                            movie.backdropPath,
                            movie.id
                        )
                    }!!
                    _topRatedMovies.value = dataList
                }
                is Result.Error ->
                    showSnackBar.value = topRatedResult.message
            }
            when (popularResult) {
                is Result.Success<*> -> {
                    val dataList = (popularResult.data as LiveData<PagingData<MovieResult>>).value?.map { movie ->
                        //map the reminder data from the DB to the be ready to be displayed on the UI
                        MovieResult(
                            movie.posterPath,
                            movie.overview,
                            movie.title,
                            movie.backdropPath,
                            movie.id
                        )
                    }!!
                    _popularMovies.value = dataList
                }
                is Result.Error ->
                    showSnackBar.value = popularResult.message
            }
            when (nowPlayingResult) {
                is Result.Success<*> -> {
                    val dataList = (nowPlayingResult.data as LiveData<PagingData<MovieResult>>).value?.map { movie ->
                        //map the reminder data from the DB to the be ready to be displayed on the UI
                        MovieResult(
                            movie.posterPath,
                            movie.overview,
                            movie.title,
                            movie.backdropPath,
                            movie.id
                        )
                    }!!
                    _nowPlayingMovies.value = dataList
                }
                is Result.Error ->
                    showSnackBar.value = nowPlayingResult.message
            }
        }
//        _nowPlayingMovies = Pager(config = PagingConfig(pageSize = 20),
//            remoteMediator = MovieRemoteMediator(
//                MovieType.NOW_PLAYING,
//                getApplication<Application>().applicationContext.getString(R.string.moviedb_key),
//                database
//            )
//        ) {
//            database.movieDao().getMovies()
//        }.liveData.cachedIn(viewModelScope) as MutableLiveData<PagingData<MovieResult>>
    }

    fun displayMovieDetails(movie: MovieResult) {
        _navigateToSelectedMovie.value = movie
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }
}