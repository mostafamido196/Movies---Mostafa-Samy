package com.samy.mostafasamy.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.samy.mostafasamy.ui.base.BaseViewModel
import com.samy.mostafasamy.data.remote.MovieServices
import com.samy.mostafasamy.di.BaseApp
import com.samy.mostafasamy.pojo.model.Popular
import com.samy.mostafasamy.pojo.response.PopularResponse
import com.samy.mostafasamy.utils.NetworkState
import com.samy.mostafasamy.utils.Utils
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
//            if (Utils.isInternetAvailable()) {
//                Log.d("hamoly","if ")
                runApi<PopularResponse, Popular>(
                    _popularMovieStateFlow,
                    repository.getPopularMovie(authorization),
                    converter = { data: PopularResponse -> convertToPopular(data) },
                    caching = { data: List<Popular> -> cachingPopular(data) },
//                    getCaching = { getPopularCached() }
                )
//            }else{
//                Log.d("hamoly","else ")
//                NetworkState.Result(getPopularCached())
//            }
        }

    }

    private suspend fun getPopularCached(): List<Popular> {
        return BaseApp.database.popularDao().getAllPopular()
    }

    private fun cachingPopular(populars: List<Popular>) {
        // check if the 4 hours old
        viewModelScope.launch {
            // cache
            populars.map { popular ->
                try {
                    BaseApp.database.popularDao().insertPopular(popular)

                }catch (e:Exception){
                    Log.d("hamoly","cachingPopular: e: ${e.message}")
                }
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

    // called from fragment
    public fun popularCached() {
        _popularMovieStateFlow.value = NetworkState.Loading
        viewModelScope.launch {
            _popularMovieStateFlow.value = NetworkState.Result(BaseApp.database.popularDao().getAllPopular())
        }
    }
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