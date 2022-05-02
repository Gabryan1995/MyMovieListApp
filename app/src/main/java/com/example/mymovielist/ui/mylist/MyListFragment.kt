package com.example.mymovielist.ui.mylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymovielist.base.BaseFragment
import com.example.mymovielist.databinding.FragmentMylistBinding
import org.koin.android.ext.android.inject

class MyListFragment : BaseFragment() {

    override val _viewModel: MyListViewModel by inject()
    private lateinit var binding: FragmentMylistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMylistBinding.inflate(layoutInflater)

        binding.viewModel = _viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
    }
}