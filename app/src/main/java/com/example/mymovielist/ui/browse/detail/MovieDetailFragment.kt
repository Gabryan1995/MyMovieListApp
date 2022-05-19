package com.example.mymovielist.ui.browse.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mymovielist.databinding.FragmentMovieDetailBinding

@ExperimentalStdlibApi
class MovieDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentMovieDetailBinding.inflate(inflater)

        val movie = MovieDetailFragmentArgs.fromBundle(arguments!!).movie
        val viewModelFactory = MovieDetailViewModelFactory(movie, application)

        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel::class.java)

        binding.lifecycleOwner = this
        return binding.root
    }
}