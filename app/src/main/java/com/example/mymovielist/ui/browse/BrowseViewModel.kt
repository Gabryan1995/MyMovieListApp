package com.example.mymovielist.ui.browse

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymovielist.R
import com.example.mymovielist.base.BaseViewModel
import com.example.mymovielist.data.dto.MovieDTO
import com.example.mymovielist.data.dto.MovieResult
import com.example.mymovielist.network.MovieApi
import kotlinx.coroutines.launch
import retrofit2.http.Url

enum class MoviesApiStatus { LOADING, ERROR, DONE }

@ExperimentalStdlibApi
class BrowseViewModel(app: Application) : BaseViewModel(app) {

    private val _status = MutableLiveData<MoviesApiStatus>()
    val status: LiveData<MoviesApiStatus>
        get() = _status

    private val _topRatedMovies = MutableLiveData<MovieDTO>()
    val topRatedMovies: LiveData<MovieDTO>
        get() = _topRatedMovies

    private val _topRatedPage = MutableLiveData<Int>()
    val topRatedPage: LiveData<Int>
        get() = _topRatedPage

    private val _popularMovies = MutableLiveData<MovieDTO>()
    val popularMovies: LiveData<MovieDTO>
        get() = _popularMovies

    private val _popularPage = MutableLiveData<Int>()
    val popularPage: LiveData<Int>
        get() = _popularPage

    private val _nowPlayingMovies = MutableLiveData<MovieDTO>()
    val nowPlayingMovies: LiveData<MovieDTO>
        get() = _nowPlayingMovies

    private val _nowPlayingPage = MutableLiveData<Int>()
    val nowPlayingPage: LiveData<Int>
        get() = _nowPlayingPage

    private val _navigateToSelectedMovie = MutableLiveData<MovieResult?>()
    val navigateToSelectedMovie: MutableLiveData<MovieResult?>
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
                _topRatedMovies.value = MovieApi.retrofitService.getTopRated(getApplication<Application>().resources.getString(R.string.moviedb_key), topRatedPage.value)
                _status.value = MoviesApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MoviesApiStatus.ERROR
                _topRatedMovies.value = MovieDTO()
            }
        }
    }

    private fun getPopular() {
        viewModelScope.launch {
            _status.value = MoviesApiStatus.LOADING
            try {
                _popularMovies.value = MovieApi.retrofitService.getPopular(getApplication<Application>().resources.getString(R.string.moviedb_key), popularPage.value)
                _status.value = MoviesApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MoviesApiStatus.ERROR
                _popularMovies.value = MovieDTO()
            }
        }
    }

    private fun getNowPlaying() {
        viewModelScope.launch {
            _status.value = MoviesApiStatus.LOADING
            try {
                _nowPlayingMovies.value = MovieApi.retrofitService.getNowPlaying(getApplication<Application>().resources.getString(R.string.moviedb_key), nowPlayingPage.value)
                _status.value = MoviesApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MoviesApiStatus.ERROR
                _nowPlayingMovies.value = MovieDTO()
            }
        }
    }

    fun displayMovieDetails(movie: MovieResult) {
        _navigateToSelectedMovie.value = movie
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }
}