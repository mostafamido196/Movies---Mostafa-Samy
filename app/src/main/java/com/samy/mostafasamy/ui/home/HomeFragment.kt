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
import androidx.recyclerview.widget.GridLayoutManager
import com.samy.mostafasamy.R
import com.samy.mostafasamy.databinding.FragmentHomeBinding
import com.samy.mostafasamy.di.BaseApp
import com.samy.mostafasamy.pojo.model.Popular
import com.samy.mostafasamy.utils.NetworkState
import com.samy.mostafasamy.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var adapter: CategoryAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var lastNavTVSelected: TextView? = null


    private lateinit var popularArr: List<Popular>
//    private lateinit var topRateArr: List<String> // Books Titles

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
        }
        binding.topRatedTv.setOnClickListener {
            selectCategory(binding.topRatedTv)
            lifecycleScope.launch {

                Log.d(
                    "hamoly",
                    "binding.topRatedTv.setOnClickListener: BaseApp.database.popularDao().getAllPopular():${
                        BaseApp.database.popularDao().getAllPopular()
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
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvShowCategory.setLayoutManager(layoutManager)
        binding.rvShowCategory.adapter = adapter

    }

    private fun selectCategory(textView: TextView) {
        lastNavTVSelected = textView
        if (textView == binding.popularTv) {
            binding.topRatedLine.visibility = View.INVISIBLE
            binding.popularLine.visibility = View.VISIBLE
            adapter.submitList(popularArr)
        } else if (textView == binding.topRatedTv) {
            binding.popularLine.visibility = View.INVISIBLE
            binding.topRatedLine.visibility = View.VISIBLE
            // adapter.submitList(topRateArr) // todo
        }
    }

    private fun goto() {


    }

    private fun data() {
        try {
            viewModel.getPopular()
//        viewModel.getTopRate()

        } catch (e: Exception) {
            Log.d("hamoly", "e: ${e.message}")
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.popularMovieSateFlow.collect {
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
                        handleResult(it.response as List<Popular>)

                    }
                }

            }


        }
        /*lifecycleScope.launch {
            viewModel.topRateMovieSateFlow.collect {
                Log.d("hamoly", "it.msg: ${it}")
                when (it) {
                    is NetworkState.Idle -> {
                        return@collect
                    }

                    is NetworkState.Loading -> {
//                        visProgress(true)
                    }

                    is NetworkState.Error -> {
//                        visProgress(false)
//                        it.handleErrors(mContext, null)
                    }

                    is NetworkState.Result<*> -> {
//                        visProgress(false)
                        handleResult(it.response as TopRatedResponse)

                    }
                }

            }


        }*/
    }

    fun <T> handleResult(response: T) {
        when (response) {
            is List<*> -> {
                uiRvPopular(response as List<Popular>)//convertToPopular(response.results) as List<Popular>)
            }
        }
    }

    private fun showProgress(b: Boolean) {
        if (b) {
            binding.progressbar.visibility = View.VISIBLE
            binding.rvShowCategory.visibility = View.GONE
        } else {
            binding.progressbar.visibility = View.GONE
            binding.rvShowCategory.visibility = View.VISIBLE
        }
    }

    private fun uiRvPopular(results: List<Popular>) {
        adapter.submitList(results)
        popularArr = results
    }

//    private fun convertToPopular(popularResponse: List<Result>): List<Popular> {
//        val populars = ArrayList<Popular>()
//        popularResponse.forEach { movie ->
//            populars.add(Popular(movie.id, movie.title, movie.poster_path,movie.release_date))
//        }
//        return populars
//    }
}