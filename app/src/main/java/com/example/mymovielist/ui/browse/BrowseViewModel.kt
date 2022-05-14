package com.example.mymovielist.ui.browse

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymovielist.base.BaseViewModel
import com.example.mymovielist.data.dto.MovieDTO
import com.example.mymovielist.network.MovieApi
import kotlinx.coroutines.launch
import retrofit2.http.Url

enum class MoviesApiStatus { LOADING, ERROR, DONE }

class BrowseViewModel(app: Application) : BaseViewModel(app) {

    private val _status = MutableLiveData<MoviesApiStatus>()
    val status: LiveData<MoviesApiStatus>
        get() = _status

    private val _topRatedMovies = MutableLiveData<List<MovieDTO>>()
    val topRatedMovies: LiveData<List<MovieDTO>>
        get() = _topRatedMovies

    private val _topRatedPage = MutableLiveData<Int>()
    val topRatedPage: LiveData<Int>
        get() = _topRatedPage

    private val _popularMovies = MutableLiveData<List<MovieDTO>>()
    val popularMovies: LiveData<List<MovieDTO>>
        get() = _popularMovies

    private val _popularPage = MutableLiveData<Int>()
    val popularPage: LiveData<Int>
        get() = _popularPage

    private val _nowPlayingMovies = MutableLiveData<List<MovieDTO>>()
    val nowPlayingMovies: LiveData<List<MovieDTO>>
        get() = _nowPlayingMovies

    private val _nowPlayingPage = MutableLiveData<Int>()
    val nowPlayingPage: LiveData<Int>
        get() = _nowPlayingPage

    private val _navigateToSelectedMovie = MutableLiveData<MovieDTO?>()
    val navigateToSelectedMovie: MutableLiveData<MovieDTO?>
        get() = _navigateToSelectedMovie

    init {
        _topRatedPage.value = 1
        _popularPage.value = 1
        _nowPlayingPage.value = 1
        getTopRated()
        getPopular()
        getNowPlaying()
    }

    private fun getTopRated() {
        viewModelScope.launch {
            _status.value = MoviesApiStatus.LOADING
            try {
                _topRatedMovies.value = MovieApi.retrofitService.getTopRated(topRatedPage.value)
                _status.value = MoviesApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MoviesApiStatus.ERROR
                _topRatedMovies.value = ArrayList()
            }
        }
    }

    private fun getPopular() {
        viewModelScope.launch {
            _status.value = MoviesApiStatus.LOADING
            try {
                _popularMovies.value = MovieApi.retrofitService.getPopular(popularPage.value)
                _status.value = MoviesApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MoviesApiStatus.ERROR
                _popularMovies.value = ArrayList()
            }
        }
    }

    private fun getNowPlaying() {
        viewModelScope.launch {
            _status.value = MoviesApiStatus.LOADING
            try {
                _nowPlayingMovies.value = MovieApi.retrofitService.getNowPlaying(nowPlayingPage.value)
                _status.value = MoviesApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MoviesApiStatus.ERROR
                _nowPlayingMovies.value = ArrayList()
            }
        }
    }

    fun displayMovieDetails(movie: MovieDTO) {
        _navigateToSelectedMovie.value = movie
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }
}