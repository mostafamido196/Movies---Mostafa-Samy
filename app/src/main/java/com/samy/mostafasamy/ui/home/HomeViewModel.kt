package com.samy.mostafasamy.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.samy.mostafasamy.data.remote.MovieServices
import com.samy.mostafasamy.di.BaseApp
import com.samy.mostafasamy.pojo.model.Popular
import com.samy.mostafasamy.pojo.response.PopularResponse
import com.samy.mostafasamy.ui.base.BaseViewModel
import com.samy.mostafasamy.utils.NetworkState
import com.samy.mostafasamy.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val repository: HomeRepository) :
    BaseViewModel() {

    private val _popularMovieStateFlow = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val popularMovieSateFlow get() = _popularMovieStateFlow




    fun getPopular() {
        _popularMovieStateFlow.value = NetworkState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            if (Utils.isInternetAvailable()) {
                Log.d("hamoly", " if (Utils.isInternetAvailable())")
                runApi<PopularResponse, List<Popular>>(
                    _popularMovieStateFlow,
                    repository.getPopularMovie(authorization),
                    converter = { data: PopularResponse -> convertToPopular(data) },
                    caching = { data: List<Popular> -> cachingPopular(data) },
                )
            } else {
                Log.d("hamoly", " else")
                popularCached()

            }
        }

    }


    private fun cachingPopular(populars: List<Popular>) {
        if (spendForeHoursOrNull())
            viewModelScope.launch {
                // cache
                populars.map { popular ->
                    try {
                        BaseApp.database.popularDao().insertPopular(popular)

                    } catch (e: Exception) {
                        Log.d("hamoly", "cachingPopular: e: ${e.message}")
                    }
                }
            }
    }

    private fun spendForeHoursOrNull(): Boolean {
        if (!Utils.getSharedPreferencesBoolean(application, "CachingFile", "isCached", false)) {
            return true
        }
        val oldDay = Utils.getSharedPreferencesInt(application, "CachingFile", "day", 1)
        val oldHour = Utils.getSharedPreferencesInt(application, "CachingFile", "hour", 1)
        val calendar: Calendar = Calendar.getInstance()
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar[Calendar.HOUR_OF_DAY]
        if (day != oldDay || (day == oldDay && hour <= oldHour - 3)) {
            return true
        }
        Utils.setSharedPreferencesBoolean(application, "CachingFile", "isCached", true)
        Utils.getSharedPreferencesInt(application, "CachingFile", "day", day)
        Utils.getSharedPreferencesInt(application, "CachingFile", "hour", hour)
        return false

    }

    private fun convertToPopular(popularResponse: PopularResponse): List<Popular> {
        val populars = ArrayList<Popular>()
        popularResponse.results.forEach { movie ->
            populars.add(
                Popular(
                    movie.id,
                    movie.title,
                    movie.poster_path,
                    movie.release_date,
                    movie.overview,
                    movie.popularity,
                    movie.vote_average,
                    movie.vote_count
                )
            )
        }
        return populars
    }

    // called from fragment
    private fun popularCached() {
        _popularMovieStateFlow.value = NetworkState.Loading
        viewModelScope.launch {
            _popularMovieStateFlow.value =
                NetworkState.Result(BaseApp.database.popularDao().getAllPopular())
        }
    }
}

class HomeRepository @Inject
constructor(private val movieServices: MovieServices) {

    suspend fun getPopularMovie(
        authorization: String,
    ) = movieServices.getPopular(authorization)




}