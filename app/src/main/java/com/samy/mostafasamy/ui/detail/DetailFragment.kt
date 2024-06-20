package com.samy.mostafasamy.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.samy.mostafasamy.R
import com.samy.mostafasamy.databinding.FragmentDetailBinding
import com.samy.mostafasamy.pojo.model.Detail
import com.samy.mostafasamy.utils.Constants
import com.samy.mostafasamy.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        data()
        observe()
    }

    private fun data() {
        val id = requireArguments().getLong(Constants.MOVIE_ID, 1022789)!!.toInt()
        viewModel.getDetail(id)
    }

    private fun setup() {


    }
    private fun observe() {
        lifecycleScope.launch {
            viewModel.detailMovieSateFlow.collect {
                Log.d("hamoly", "it.msg: ${it}")
                when (it) {
                    is NetworkState.Idle -> {
                        return@collect
                    }

                    is NetworkState.Loading -> {
                        showProgress(true)
                    }

                    is NetworkState.Error -> {
                        showProgress(false)
                        uiLocal()
//                        Toast.makeText(requireContext(),it.msg, Toast.LENGTH_SHORT).show()
                    }

                    is NetworkState.Result<*> -> {
                        showProgress(false)
                        handleResult(it.response as Detail)

                    }
                }

            }
        }

    }

    private fun uiLocal() {
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
    }

    fun <T> handleResult(response: T) {
        when (response) {
            is Detail -> {
                ui(response)
            }
        }
    }

    private fun ui(data: Detail) {
        Log.d(
            "hamoly",
            "detail fragment id: ${data.id}"+
                    ", overview: ${data.overview}"+
                    ", popularity: ${data.popularity}"+
                    ", title: ${data.title}"+
                    ", poster_path: ${data.poster_path}"+
                    ", release_date: ${data.release_date}"+
                    ", vote_average: ${data.vote_average}"+
                    ", vote_count: ${data.vote_average}"
        )
        Glide.with(binding.root.context)
            .load(Constants.POSTER_URL + data.poster_path)
            .error(R.drawable.baseline_image_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.iv)
        binding.title.text = data.title
        binding.overview.text = data.overview
        binding.time.text = "release date ${data.release_date}"
        binding.favorit.text = "popularity: ${data.popularity}"
        binding.voteAverage.text = "Vote average: ${data.vote_average}"
        binding.voteCount.text = "Vote count: ${data.vote_count}"


    }
    private fun showProgress(b: Boolean) {
        if (b) {
            binding.progressbar.visibility = View.VISIBLE
            binding.ll.visibility = View.GONE
        } else {
            binding.progressbar.visibility = View.GONE
            binding.ll.visibility = View.VISIBLE
        }
    }

}