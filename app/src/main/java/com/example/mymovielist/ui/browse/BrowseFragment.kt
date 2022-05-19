package com.example.mymovielist.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mymovielist.base.BaseFragment
import com.example.mymovielist.databinding.FragmentBrowseBinding
import org.koin.android.ext.android.inject

@ExperimentalStdlibApi
class BrowseFragment : BaseFragment() {

    override val _viewModel: BrowseViewModel by inject()
    private lateinit var binding: FragmentBrowseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowseBinding.inflate(layoutInflater)

        binding.viewModel = _viewModel

        binding.topRecyclerview.adapter = MovieGridAdapter(MovieGridAdapter.OnClickListener {
            _viewModel.displayMovieDetails(it)
        })

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