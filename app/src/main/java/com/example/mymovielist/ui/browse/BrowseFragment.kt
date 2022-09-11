package com.example.mymovielist.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.example.mymovielist.base.BaseFragment
import com.example.mymovielist.databinding.FragmentBrowseBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@ExperimentalPagingApi
@ExperimentalStdlibApi
class BrowseFragment : BaseFragment() {

    override val _viewModel: BrowseViewModel by viewModel { parametersOf(this) }
    private lateinit var binding: FragmentBrowseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowseBinding.inflate(layoutInflater)

        binding.viewModel = _viewModel

        val pagingAdapter = MovieGridAdapter(MovieGridAdapter.OnClickListener {
            _viewModel.displayMovieDetails(it)
        })

        binding.topRecyclerview.adapter = pagingAdapter

        binding.popularRecyclerview.adapter = pagingAdapter

        binding.nowplayingRecyclerview.adapter = pagingAdapter

        _viewModel.getTopRatedMovies().observe(this) { pagingData ->
            pagingAdapter.submitData(
                lifecycle,
                pagingData
            )
        }

        _viewModel.getPopularMovies().observe(this) { pagingData ->
            pagingAdapter.submitData(
                lifecycle,
                pagingData
            )
        }

        _viewModel.getNowPlayingMovies().observe(this) { pagingData ->
            pagingAdapter.submitData(
                lifecycle,
                pagingData
            )
        }

        _viewModel.navigateToSelectedMovie.observe(this, {
            if (null != it) {
                this.findNavController().navigate(BrowseFragmentDirections.actionShowDetail(it))
                _viewModel.displayMovieDetailsComplete()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
    }


}