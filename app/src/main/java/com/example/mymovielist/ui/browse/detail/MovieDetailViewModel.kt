package com.example.mymovielist.ui.browse.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymovielist.data.dto.MovieDTO

class MovieDetailViewModel(movie: MovieDTO, app: Application) : AndroidViewModel(app) {
    private val _selectedMovie = MutableLiveData<MovieDTO>()
    val selectedMovie: LiveData<MovieDTO>
        get() = _selectedMovie

    init {
        _selectedMovie.value = movie
    }
}
