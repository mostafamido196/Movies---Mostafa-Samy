package com.samy.mostafasamy.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.samy.mostafasamy.R
import com.samy.mostafasamy.databinding.ItemCategoryBinding
import com.samy.mostafasamy.pojo.model.Search
import com.samy.mostafasamy.utils.Constants
import javax.inject.Inject

class SearchAdapter @Inject constructor() :
    ListAdapter<Search, SearchAdapter.ViewHolder>(DiffCallback()) {
    class DiffCallback : DiffUtil.ItemCallback<Search>() {
        override fun areItemsTheSame(
            oldItem: Search, newItem: Search,
        ): Boolean = newItem.id == oldItem.id

        override fun areContentsTheSame(
            oldItem: Search, newItem: Search,
        ): Boolean = newItem == oldItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, Type: Int): ViewHolder =
        ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Search) {

            binding.tvName.text = data.title
            binding.tvDate.text = data.release_date


            Glide.with(binding.root.context)
                .load(Constants.POSTER_URL + data.poster_path)
                .error(R.drawable.baseline_image_24)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.iv)
            binding.root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(data)
                }
            }


        }

    }


    private var onItemClickListener: ((Search) -> Unit)? = null

    fun setOnItemClickListener(listener: (Search) -> Unit) {
        onItemClickListener = listener
    }

}
