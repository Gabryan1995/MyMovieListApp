package com.example.mymovielist.ui.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovielist.data.dto.MovieResult
import com.example.mymovielist.databinding.MovieGridItemBinding

@ExperimentalStdlibApi
class MovieGridAdapter(val onClickListener: OnClickListener) : PagingDataAdapter<MovieResult, MovieGridAdapter.MovieViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<MovieResult>() {
        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class MovieViewHolder(private var binding: MovieGridItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieResult?) {
            binding.movie = movie
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(MovieGridItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(movie!!)
        }
    }

    class OnClickListener(val clickListener: (movie: MovieResult) -> Unit) {
        fun onClick(movie: MovieResult) = clickListener(movie)
    }
}