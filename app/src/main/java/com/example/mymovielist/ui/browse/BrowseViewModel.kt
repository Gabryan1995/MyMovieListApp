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

    private val _topRatedMovies = MutableLiveData<PagingData<MovieResult>>()
    val topRatedMovies: LiveData<PagingData<MovieResult>>
        get() = _topRatedMovies

    private val _popularMovies = MutableLiveData<PagingData<MovieResult>>()
    val popularMovies: LiveData<PagingData<MovieResult>>
        get() = _popularMovies

    private val _nowPlayingMovies = MutableLiveData<PagingData<MovieResult>>()
    val nowPlayingMovies: LiveData<PagingData<MovieResult>>
        get() = _nowPlayingMovies

    private val _navigateToSelectedMovie = MutableLiveData<MovieResult?>()
    val navigateToSelectedMovie: MutableLiveData<MovieResult?>
        get() = _navigateToSelectedMovie

    init {
        loadMovies()
    }

    fun loadMovies() {
        showLoading.value = true
        _topRatedMovies.value = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(getApplication<Application>().applicationContext.getString(R.string.moviedb_key))
        }.liveData.cachedIn(viewModelScope).value

        _popularMovies.value = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(getApplication<Application>().applicationContext.getString(R.string.moviedb_key))
        }.liveData.cachedIn(viewModelScope).value

        _nowPlayingMovies.value = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(getApplication<Application>().applicationContext.getString(R.string.moviedb_key))
        }.liveData.cachedIn(viewModelScope).value

//        viewModelScope.launch {
//            //interacting with the dataSource has to be through a coroutine
//            val result = dataSource.getMovies(getApplication<Application>().applicationContext.getString(R.string.moviedb_key))
//            showLoading.postValue(false)
//            when (result) {
//                is Result.Success<*> -> {
//                    val dataList = ArrayList<MoviesPage>()
//                    dataList.addAll((result.data as List<MoviesPage>).map { movie ->
//                        //map the reminder data from the DB to the be ready to be displayed on the UI
//                        MoviesPage(
//                            movie.page,
//                            movie.results,
//                            movie.wResults,
//                            movie.totalResults,
//                            movie.totalPages,
//                            movie.id,
//                            movie.movieType
//                        )
//                    })
//                    for (movie in dataList) {
//                        when(movie.movieType) {
//                            MovieType.TOP_RATED -> _topRatedMovies.value = movie
//                            MovieType.POPULAR -> _popularMovies.value = movie
//                            MovieType.NOW_PLAYING -> _nowPlayingMovies.value = movie
//                        }
//                        _status.value = MoviesApiStatus.DONE
//                    }
//                }
//                is Result.Error -> {
//                    _topRatedMovies.value = MoviesPage(movieType = MovieType.TOP_RATED)
//                    _popularMovies.value = MoviesPage(movieType = MovieType.POPULAR)
//                    _nowPlayingMovies.value = MoviesPage(movieType = MovieType.NOW_PLAYING)
//                    _status.value = MoviesApiStatus.ERROR
//                    showSnackBar.value = result.message
//                }
//            }
//        }
    }

    override fun onCleared() {
//        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
//        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
        super.onCleared()
    }

    fun displayMovieDetails(movie: MovieResult) {
        _navigateToSelectedMovie.value = movie
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }
}

private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = "Android"