package com.example.mymovielist.ui.browse

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymovielist.R
import com.example.mymovielist.base.BaseViewModel
import com.example.mymovielist.data.MovieDataSource
import com.example.mymovielist.data.dto.*
import com.example.mymovielist.network.MovieApi
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

enum class MoviesApiStatus { LOADING, ERROR, DONE }

@ExperimentalStdlibApi
class BrowseViewModel(app: Application, private val dataSource: MovieDataSource)
    : BaseViewModel(app) {

    private val _status = MutableLiveData<MoviesApiStatus>()
    val status: LiveData<MoviesApiStatus>
        get() = _status

    private val _topRatedMovies = MutableLiveData<MoviesPage>()
    val topRatedMovies: LiveData<MoviesPage>
        get() = _topRatedMovies

    private var lastPageTopRated = false

    private val _popularMovies = MutableLiveData<MoviesPage>()
    val popularMovies: LiveData<MoviesPage>
        get() = _popularMovies

    private var lastPagePopular = false

    private val _nowPlayingMovies = MutableLiveData<MoviesPage>()
    val nowPlayingMovies: LiveData<MoviesPage>
        get() = _nowPlayingMovies

    private var lastPageNowPlaying = false

    private val _navigateToSelectedMovie = MutableLiveData<MovieResult?>()
    val navigateToSelectedMovie: MutableLiveData<MovieResult?>
        get() = _navigateToSelectedMovie

    init {
        getTopRated(1)
        getPopular(1)
        getNowPlaying(1)
    }

    fun loadMovies() {
        showLoading.value = true
        viewModelScope.launch {
            //interacting with the dataSource has to be through a coroutine
            val result = dataSource.getMovies()
            showLoading.postValue(false)
            when (result) {
                is Result.Success<*> -> {
                    val dataList = ArrayList<MoviesPage>()
                    dataList.addAll((result.data as List<MoviesPage>).map { movie ->
                        //map the reminder data from the DB to the be ready to be displayed on the UI
                        MoviesPage(
                            movie.page,
                            movie.results,
                            movie.wResults,
                            movie.totalResults,
                            movie.totalPages,
                            movie.id,
                            movie.movieType
                        )
                    })
                    for (movie in dataList) {
                        when(movie.movieType) {
                            MovieType.TOP_RATED -> _topRatedMovies.value = movie
                            MovieType.POPULAR -> _popularMovies.value = movie
                            MovieType.NOW_PLAYING -> _nowPlayingMovies.value = movie
                        }
                        _status.value = MoviesApiStatus.DONE
                    }
                }
                is Result.Error -> {
                    _topRatedMovies.value = MoviesPage(movieType = MovieType.TOP_RATED)
                    _popularMovies.value = MoviesPage(movieType = MovieType.POPULAR)
                    _nowPlayingMovies.value = MoviesPage(movieType = MovieType.NOW_PLAYING)
                    _status.value = MoviesApiStatus.ERROR
                    showSnackBar.value = result.message
                }
            }
        }
    }

    fun getTopRated(currentPage: Int) {
        viewModelScope.launch {
            _status.value = MoviesApiStatus.LOADING
            try {
                _topRatedMovies.value = MovieApi.retrofitService.getTopRated(getApplication<Application>().resources.getString(R.string.moviedb_key), currentPage)
                _topRatedMovies.value!!.movieType = MovieType.TOP_RATED
                _status.value = MoviesApiStatus.DONE
                if (currentPage == topRatedMovies.value?.totalPages)
                    lastPageTopRated = true
            } catch (e: Exception) {
                _status.value = MoviesApiStatus.ERROR
                _topRatedMovies.value = MoviesPage(movieType = MovieType.TOP_RATED)
            }
        }
    }

    fun getPopular(currentPage: Int) {
        viewModelScope.launch {
            _status.value = MoviesApiStatus.LOADING
            try {
                _popularMovies.value = MovieApi.retrofitService.getPopular(getApplication<Application>().resources.getString(R.string.moviedb_key), currentPage)
                _popularMovies.value!!.movieType = MovieType.POPULAR
                _status.value = MoviesApiStatus.DONE
                if (currentPage == popularMovies.value?.totalPages)
                    lastPagePopular = true
            } catch (e: Exception) {
                _status.value = MoviesApiStatus.ERROR
                _popularMovies.value = MoviesPage(movieType = MovieType.POPULAR)
            }
        }
    }

    fun getNowPlaying(currentPage: Int) {
        viewModelScope.launch {
            _status.value = MoviesApiStatus.LOADING
            try {
                _nowPlayingMovies.value = MovieApi.retrofitService.getNowPlaying(getApplication<Application>().resources.getString(R.string.moviedb_key), currentPage)
                _nowPlayingMovies.value!!.movieType = MovieType.NOW_PLAYING
                _status.value = MoviesApiStatus.DONE
                if (currentPage == nowPlayingMovies.value?.totalPages)
                    lastPageNowPlaying = true
            } catch (e: Exception) {
                _status.value = MoviesApiStatus.ERROR
                _nowPlayingMovies.value = MoviesPage(movieType = MovieType.NOW_PLAYING)
            }
        }
    }

    fun isLastPage_topRated(): Boolean {
        return lastPageTopRated
    }

    fun isLastPage_popular(): Boolean {
        return lastPagePopular
    }

    fun isLastPage_nowPlaying(): Boolean {
        return lastPageNowPlaying
    }

    fun displayMovieDetails(movie: MovieResult) {
        _navigateToSelectedMovie.value = movie
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }
}