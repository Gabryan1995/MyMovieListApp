package com.example.mymovielist.ui.browse.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymovielist.data.dto.MovieDTO
import com.example.mymovielist.data.dto.MovieResult

class MovieDetailViewModel(movie: MovieResult, app: Application) : AndroidViewModel(app) {
    private val _selectedMovie = MutableLiveData<MovieResult>()
    val selectedMovie: LiveData<MovieResult>
        get() = _selectedMovie

    init {
        _selectedMovie.value = movie
    }
}
