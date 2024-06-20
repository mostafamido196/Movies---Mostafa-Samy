package com.samy.mostafasamy.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.samy.mostafasamy.R
import com.samy.mostafasamy.databinding.FragmentDetailBinding
import com.samy.mostafasamy.pojo.model.Detail
import com.samy.mostafasamy.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
//        observe()
    }

    private fun setup() {
        try {
            Log.d("hamoly","requireArguments().getLong(Constants.MOVIE_ID, 1022789).toInt() :${requireArguments().getLong(Constants.MOVIE_ID, 1022789).toInt()}")

            val id = requireArguments().getLong(Constants.MOVIE_ID, 1022789)!!.toInt()
            val title = requireArguments().getString(Constants.TITLE)!!
            val overview = requireArguments().getString(Constants.OVERVIEW)!!
            val popularity = requireArguments().getDouble(Constants.POPULARITY)!!
            val posterPath = requireArguments().getString(Constants.POSTER_PATH)!!
            val releaseDate = requireArguments().getString(Constants.RELEASE_DATE)!!
            val voteAverage = requireArguments().getDouble(Constants.VOTE_AVERAGE)!!
            val voteCount = requireArguments().getInt(Constants.VOTE_COUNT)!!
            Log.d(
                "hamoly",
                "detail fragment id: ${id}"+
                        ", overview: ${overview}"+
                        ", popularity: ${popularity}"+
                        ", title: ${title}"+
                        ", poster_path: ${posterPath}"+
                        ", release_date: ${releaseDate}"+
                        ", vote_average: ${voteAverage}"+
                        ", vote_count: ${voteCount}"
            )
            val detailView = Detail(id, title, posterPath, releaseDate, overview, popularity,voteAverage,voteCount)
            Glide.with(binding.root.context)
                .load(Constants.POSTER_URL + detailView.poster_path)
                .error(R.drawable.baseline_image_24)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.iv)
            binding.title.text = detailView.title
            binding.overview.text = detailView.overview
            binding.time.text = "release date ${detailView.release_date}"
            binding.favorit.text = "popularity: ${detailView.popularity}"
            binding.voteAverage.text = "Vote average: ${detailView.vote_average}"
            binding.voteCount.text = "Vote count: ${detailView.vote_count}"
        } catch (e: Exception) {
            Log.d("hamoly", "e: ${e.message}")
        }

    }

    private fun showProgress(b: Boolean) {
        if (b) {
            binding.progressbar.visibility = View.VISIBLE
//            binding.rvSearch.visibility = View.GONE
        } else {
            binding.progressbar.visibility = View.GONE
//            binding.rvSearch.visibility = View.VISIBLE
        }
    }

}