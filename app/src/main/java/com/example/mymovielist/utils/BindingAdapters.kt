package com.example.mymovielist.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mymovielist.R
import com.example.mymovielist.data.dto.MovieDTO
import com.example.mymovielist.network.*
import com.example.mymovielist.ui.browse.MovieGridAdapter
import com.example.mymovielist.ui.browse.MoviesApiStatus

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = IMAGE_URL + Uri.parse(imgUrl.toString())
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions())
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_broken_image)
            .into(imgView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, movies: List<MovieDTO>?) {
    val adapter = recyclerView.adapter as MovieGridAdapter
    adapter.submitList(movies)
}

@BindingAdapter("movieLoadingStatus")
fun bindStatus(statusImageView: ImageView, status: MoviesApiStatus?) {
    when (status) {
        MoviesApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_loading)
        }
        MoviesApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_broken_image)
        }
        MoviesApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}