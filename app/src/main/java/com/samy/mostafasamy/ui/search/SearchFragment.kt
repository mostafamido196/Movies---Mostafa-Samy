package com.samy.mostafasamy.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.samy.mostafasamy.R
import com.samy.mostafasamy.databinding.FragmentSearchBinding
import com.samy.mostafasamy.pojo.model.Search
import com.samy.mostafasamy.utils.Constants
import com.samy.mostafasamy.utils.NetworkState
import com.samy.mostafasamy.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var adapter: SearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
        goto()
        onClick()
        observe()
    }

    private fun goto() {
        adapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putLong(Constants.MOVIE_ID, it.id)
            bundle.putString(Constants.OVERVIEW, it.overview)
            bundle.putDouble(Constants.POPULARITY, it.popularity)
            bundle.putString(Constants.TITLE, it.title)
            bundle.putString(Constants.POSTER_PATH, it.poster_path)
            bundle.putString(Constants.RELEASE_DATE, it.release_date)
            bundle.putDouble(Constants.VOTE_AVERAGE, it.vote_average)
            bundle.putInt(Constants.VOTE_COUNT, it.vote_count)
            findNavController().navigate(R.id.detailFragment, bundle)
        }
    }

    private fun setup() {
        //rv
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvSearch.setLayoutManager(layoutManager)
        binding.rvSearch.adapter = adapter
    }

    private fun onClick() {
        binding.ivSearch.setOnClickListener {
            if (Utils.isInternetAvailable()) {
                if (binding.etSearch.text.toString().isNotEmpty()) {
                    showProgress(true)
                    viewModel.search(binding.etSearch.text.toString())
                }
            } else {
                Toast.makeText(requireContext(),"Check the internet Connection",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.searchMovieSateFlow.collect {
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
                        handleResult(it.response as List<Search>)

                    }
                }

            }
        }

    }

    fun <T> handleResult(response: T) {
        when (response) {
            is List<*> -> {
                uiRvSearch(response as List<Search>)
            }
        }
    }

    private fun uiRvSearch(list: List<Search>) {
        adapter.submitList(list)
    }

    private fun showProgress(b: Boolean) {
        if (b) {
            binding.progressbar.visibility = View.VISIBLE
            binding.rvSearch.visibility = View.GONE
        } else {
            binding.progressbar.visibility = View.GONE
            binding.rvSearch.visibility = View.VISIBLE
        }
    }

}