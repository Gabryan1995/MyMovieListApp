package com.example.mymovielist.utils

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mymovielist.R
import com.example.mymovielist.data.dto.MovieResult
import com.example.mymovielist.data.dto.MoviesPage
import com.example.mymovielist.network.*
import com.example.mymovielist.ui.browse.MovieGridAdapter

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