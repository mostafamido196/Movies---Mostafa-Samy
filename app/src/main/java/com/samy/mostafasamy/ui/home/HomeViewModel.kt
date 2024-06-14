package com.samy.mostafasamy.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.samy.mostafasamy.ui.base.BaseViewModel
import com.samy.mostafasamy.data.remote.MovieServices
import com.samy.mostafasamy.di.BaseApp
import com.samy.mostafasamy.pojo.model.Popular
import com.samy.mostafasamy.pojo.response.PopularResponse
import com.samy.mostafasamy.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) :
    BaseViewModel() {

    private val _popularMovieStateFlow = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val popularMovieSateFlow get() = _popularMovieStateFlow


    private val _topRateMovieStateFlow = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val topRateMovieSateFlow get() = _topRateMovieStateFlow


    fun getPopular() {
            _popularMovieStateFlow.value = NetworkState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                runApi(
                    _popularMovieStateFlow,
                    repository.getPopularMovie(authorization),
                    converter = { data: PopularResponse -> convertToPopular(data) },
                    caching = { data: List<Popular> -> caching(data) },
                    getCaching = { getPopularCached() }
                )

            }

    }

    private suspend fun getPopularCached(): List<Popular> {
        return BaseApp.database.popularDao().getAllPopular()
    }

    private fun caching(populars: List<Popular>) {
        // check if the 4 hours old
        viewModelScope.launch {
            // cache
            populars.map { popular ->
                BaseApp.database.popularDao().insertPopular(popular)
            }
        }
    }

    private fun convertToPopular(popularResponse: PopularResponse): List<Popular> {
        val populars = ArrayList<Popular>()
        popularResponse.results.forEach { movie ->
            populars.add(Popular(movie.id, movie.title, movie.poster_path, movie.release_date))
        }
        return populars
    }

//    fun getTopRate() {
//
//        _topRateMovieStateFlow.value = NetworkState.Loading
//
//        viewModelScope.launch(Dispatchers.IO) {
//            runApi(
//                _topRateMovieStateFlow,
//                repository.getTopRateMovie(authorization)
//            )
//        }

//    }


}

class HomeRepository @Inject
constructor(private val movieServices: MovieServices) {

    suspend fun getPopularMovie(
        authorization: String,
    ) = movieServices.getPopular(authorization)


    suspend fun getTopRateMovie(
        authorization: String,
    ) = movieServices.getTopRated(authorization)


}