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
import com.example.mymovielist.data.dto.Result.*
import com.example.mymovielist.data.local.MoviePagingSource
import kotlinx.coroutines.launch
import java.util.*

enum class MoviesApiStatus { LOADING, ERROR, DONE }

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

    init {
        loadMovies()
    }

    private fun loadMovies() {
        showLoading.value = true

        _topRatedMovies = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(getApplication<Application>().applicationContext.getString(R.string.moviedb_key))
        }.liveData.cachedIn(viewModelScope) as MutableLiveData<PagingData<MovieResult>>

        _popularMovies = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(getApplication<Application>().applicationContext.getString(R.string.moviedb_key))
        }.liveData.cachedIn(viewModelScope) as MutableLiveData<PagingData<MovieResult>>

        _nowPlayingMovies = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(getApplication<Application>().applicationContext.getString(R.string.moviedb_key))
        }.liveData.cachedIn(viewModelScope) as MutableLiveData<PagingData<MovieResult>>
    }

    fun displayMovieDetails(movie: MovieResult) {
        _navigateToSelectedMovie.value = movie
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }
}