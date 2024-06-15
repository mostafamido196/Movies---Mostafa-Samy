package com.samy.mostafasamy.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.samy.mostafasamy.R
import com.samy.mostafasamy.databinding.FragmentHomeBinding
import com.samy.mostafasamy.di.BaseApp
import com.samy.mostafasamy.pojo.model.Popular
import com.samy.mostafasamy.pojo.model.TopRating
import com.samy.mostafasamy.utils.Constants
import com.samy.mostafasamy.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var popularAdapter: PopularAdapter

    @Inject
    lateinit var topRatingAdapter: TopRatingAdapter
    private val popularViewModel: HomeViewModel by viewModels()
    private val topRatingViewModel: TopRatingViewModel by viewModels()
    private var lastNavTVSelected: TextView? = null


    private lateinit var popularArr: List<Popular>
    private lateinit var topRateArr: List<TopRating>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        setup()
        data()
        goto()
        onClick()
        observe()
    }

    private fun onClick() {
        binding.popularTv.setOnClickListener {
            selectCategory(binding.popularTv)
            lifecycleScope.launch {

                Log.d(
                    "hamoly",
                    "binding.topRatedTv.setOnClickListener: BaseApp.database.popularDao().getAllPopular():${
                        BaseApp.database.popularDao().getAllPopular()
                    }"
                )
            }
        }
        binding.topRatedTv.setOnClickListener {
            selectCategory(binding.topRatedTv)
            lifecycleScope.launch {

                Log.d(
                    "hamoly",
                    "binding.topRatedTv.setOnClickListener: BaseApp.database.topRatingDao().getAllTopRating():${
                        BaseApp.database.topRatingDao().getAllTopRating()
                    }"
                )
            }
        }
    }

    private fun setup() {
        //initial state
        showProgress(true)
        lastNavTVSelected = binding.popularTv

        //rv
        binding.rvShowPopular.setLayoutManager(GridLayoutManager(context, 2))
        binding.rvShowPopular.adapter = popularAdapter

        binding.rvShowTopRating.setLayoutManager(GridLayoutManager(context, 2))
        binding.rvShowTopRating.adapter = topRatingAdapter

    }

    private fun selectCategory(textView: TextView) {
        lastNavTVSelected = textView
        if (textView == binding.popularTv) {
            binding.topRatedLine.visibility = View.INVISIBLE
            binding.popularLine.visibility = View.VISIBLE
            binding.rvShowPopular.visibility = View.VISIBLE
            binding.rvShowTopRating.visibility = View.INVISIBLE
            popularAdapter.submitList(popularArr)
            Log.d("hamoly", "popularAdapter.currentList : ${popularAdapter.currentList}")
            Log.d(
                "hamoly",
                "binding.rvShowPopular.visibility : ${binding.rvShowPopular.visibility}"
            )
        } else if (textView == binding.topRatedTv) {
            binding.popularLine.visibility = View.INVISIBLE
            binding.topRatedLine.visibility = View.VISIBLE
            binding.rvShowPopular.visibility = View.INVISIBLE
            binding.rvShowTopRating.visibility = View.VISIBLE
            topRatingAdapter.submitList(topRateArr)
            Log.d("hamoly", "topRatingAdapter.currentList : ${topRatingAdapter.currentList}")
            Log.d(
                "hamoly",
                "binding.rvShowTopRating.visibility : ${binding.rvShowTopRating.visibility}"
            )
        }
    }

    private fun goto() {
        binding.tvSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        popularAdapter.setOnItemClickListener {
            Log.d(
                "hamoly",
                "fragment home Constants.POSTER_URL + detailView.poster_path: ${Constants.POSTER_URL + it.poster_path}"
            )
            Log.d(
                "hamoly",
                "home fragment it.id: ${it.id}" +
                        ", overview: ${it.overview}" +
                        ", popularity: ${it.popularity}" +
                        ", title: ${it.title}" +
                        ", poster_path: ${it.poster_path}" +
                        ", release_date: ${it.release_date}" +
                        ", vote_average: ${it.vote_average}" +
                        ", vote_count: ${it.vote_count}"
            )
            val bundle = Bundle()
            bundle.putLong(Constants.MOVIE_ID, it.id)
            bundle.putString(Constants.OVERVIEW, it.overview)
            bundle.putDouble(Constants.POPULARITY, it.popularity)
            bundle.putString(Constants.TITLE, it.title)//
            bundle.putString(Constants.POSTER_PATH, it.poster_path)//
            bundle.putString(Constants.RELEASE_DATE, it.release_date)
            bundle.putDouble(Constants.VOTE_AVERAGE, it.vote_average)
            bundle.putInt(Constants.VOTE_COUNT, it.vote_count)
            findNavController().navigate(R.id.detailFragment, bundle)
        }
        topRatingAdapter.setOnItemClickListener {
            Log.d(
                "hamoly",
                "fragment home Constants.POSTER_URL + detailView.poster_path: ${Constants.POSTER_URL + it.poster_path}"
            )
            Log.d(
                "hamoly",
                "home fragment it.id: ${it.id}" +
                        ", overview: ${it.overview}" +
                        ", popularity: ${it.popularity}" +
                        ", title: ${it.title}" +
                        ", poster_path: ${it.poster_path}" +
                        ", release_date: ${it.release_date}" +
                        ", vote_average: ${it.vote_average}" +
                        ", vote_count: ${it.vote_count}"
            )
            val bundle = Bundle()
            bundle.putLong(Constants.MOVIE_ID, it.id)
            bundle.putString(Constants.OVERVIEW, it.overview)
            bundle.putDouble(Constants.POPULARITY, it.popularity)
            bundle.putString(Constants.TITLE, it.title)//
            bundle.putString(Constants.POSTER_PATH, it.poster_path)//
            bundle.putString(Constants.RELEASE_DATE, it.release_date)
            bundle.putDouble(Constants.VOTE_AVERAGE, it.vote_average)
            bundle.putInt(Constants.VOTE_COUNT, it.vote_count)
            findNavController().navigate(R.id.detailFragment, bundle)
        }

    }

    private fun data() {
        popularViewModel.getPopular()
        topRatingViewModel.getTopRating()

    }

    private fun observe() {
        lifecycleScope.launch {
            popularViewModel.popularMovieSateFlow.collect {
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
//                        it.handleErrors(mContext, null)
                    }

                    is NetworkState.Result<*> -> {
                        showProgress(false)
                        handleResultPopular(it.response as List<Popular>)

                    }
                }

            }
        }
        lifecycleScope.launch {
            topRatingViewModel.topRateMovieSateFlow.collect {
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
                        //                        it.handleErrors(mContext, null)
                    }

                    is NetworkState.Result<*> -> {
                        showProgress(false)
                        handleResultTopRating(it.response as List<TopRating>)

                    }
                }

            }
        }


    }

    fun <T> handleResultPopular(response: T) {
        when (response) {
            is List<*> -> {
                uiRvPopular(response as List<Popular>)//convertToPopular(response.results) as List<Popular>)
            }
        }
    }

    fun <T> handleResultTopRating(response: T) {
        when (response) {
            is List<*> -> {
                uiRvTopRating(response as List<TopRating>)//convertToPopular(response.results) as List<Popular>)
            }
        }
    }

    private fun uiRvPopular(results: List<Popular>) {
        popularAdapter.submitList(results)
        popularArr = results
        popularAdapter.notifyDataSetChanged()
    }

    private fun uiRvTopRating(results: List<TopRating>) {
        topRatingAdapter.submitList(results)
        topRateArr = results
        topRatingAdapter.notifyDataSetChanged()
    }

    private fun showProgress(b: Boolean) {
        if (b) {
            binding.progressbar.visibility = View.VISIBLE
            binding.rvShowPopular.visibility = View.GONE
            binding.rvShowTopRating.visibility = View.GONE
        } else {
            binding.progressbar.visibility = View.GONE
            binding.rvShowPopular.visibility = View.VISIBLE
            binding.rvShowTopRating.visibility = View.INVISIBLE
        }
    }

//    private fun convertToPopular(popularResponse: List<Result>): List<Popular> {
//        val populars = ArrayList<Popular>()
//        popularResponse.forEach { movie ->
//            populars.add(Popular(movie.id, movie.title, movie.poster_path,movie.release_date))
//        }
//        return populars
//    }
}