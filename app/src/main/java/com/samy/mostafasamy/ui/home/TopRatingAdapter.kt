package com.samy.mostafasamy.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.samy.mostafasamy.R
import com.samy.mostafasamy.databinding.ItemCategoryBinding
import com.samy.mostafasamy.pojo.model.TopRating
import com.samy.mostafasamy.utils.Constants
import javax.inject.Inject


class TopRatingAdapter @Inject constructor() :
    ListAdapter<TopRating, TopRatingAdapter.ViewHolder>(DiffCallback()) {
    class DiffCallback : DiffUtil.ItemCallback<TopRating>() {
        override fun areItemsTheSame(
            oldItem: TopRating, newItem: TopRating,
        ): Boolean = newItem.id == oldItem.id

        override fun areContentsTheSame(
            oldItem: TopRating, newItem: TopRating,
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
        try {
            holder.bind(getItem(position))
        } catch (e: Exception) {
            Log.d("hamoly", "onBindViewHolder e :$e")
            Log.d("hamoly", "onBindViewHolder getItem(position) :${getItem(position)}")
            Log.d("hamoly", "onBindViewHolder getItemViewType(position) :${getItemViewType(position)}")
        }
    }

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TopRating) {
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


    private var onItemClickListener: ((TopRating) -> Unit)? = null

    fun setOnItemClickListener(listener: (TopRating) -> Unit) {
        onItemClickListener = listener
    }

}
