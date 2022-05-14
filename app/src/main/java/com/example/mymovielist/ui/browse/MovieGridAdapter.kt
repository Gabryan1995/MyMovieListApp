package com.example.mymovielist.ui.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovielist.data.dto.MovieDTO
import com.example.mymovielist.databinding.MovieGridItemBinding

class MovieGridAdapter(val onClickListener: OnClickListener) : ListAdapter<MovieDTO, MovieGridAdapter.MovieViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<MovieDTO>() {
        override fun areItemsTheSame(oldItem: MovieDTO, newItem: MovieDTO): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MovieDTO, newItem: MovieDTO): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class MovieViewHolder(private var binding: MovieGridItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieDTO) {
            binding.movie = movie
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGridAdapter.MovieViewHolder {
        return MovieViewHolder(MovieGridItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MovieGridAdapter.MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(movie)
        }
    }

    class OnClickListener(val clickListener: (movie: MovieDTO) -> Unit) {
        fun onClick(movie: MovieDTO) = clickListener(movie)
    }
}