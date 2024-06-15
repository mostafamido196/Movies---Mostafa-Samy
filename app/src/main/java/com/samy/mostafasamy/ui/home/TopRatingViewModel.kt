package com.samy.mostafasamy.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.samy.mostafasamy.data.remote.MovieServices
import com.samy.mostafasamy.di.BaseApp
import com.samy.mostafasamy.pojo.model.TopRating
import com.samy.mostafasamy.pojo.response.TopRatedResponse
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
class TopRatingViewModel @Inject constructor(
    private val application: Application,
    private val repository: TopRatingRepository) :
    BaseViewModel() {

    private val _topRateMovieStateFlow = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val topRateMovieSateFlow get() = _topRateMovieStateFlow


    fun getTopRating() {
        _topRateMovieStateFlow.value = NetworkState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            if (Utils.isInternetAvailable()) {
                Log.d("hamoly", " if (Utils.isInternetAvailable())")
                runApi<TopRatedResponse, List<TopRating>>(
                    _topRateMovieStateFlow,
                    repository.getTopRateMovie(authorization),
                    converter = { data: TopRatedResponse -> convertToTopRating(data) },
                    caching = { data: List<TopRating> -> cachingTopRating(data) },
                )
            } else {
                Log.d("hamoly", " else")
                TopRatingCached()

            }
        }

    }


    private fun cachingTopRating(topRating: List<TopRating>) {
        if (spendForeHoursOrNull())
            viewModelScope.launch {
                // cache
                topRating.map {
                    try {
                        BaseApp.database.topRatingDao().insertTopRating(it)

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

    private fun convertToTopRating(topRatedResponse: TopRatedResponse): List<TopRating> {
        val topRating = ArrayList<TopRating>()
        topRatedResponse.results.forEach { movie ->
            topRating.add(
                TopRating(
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
        return topRating
    }

    // called from fragment
    public fun TopRatingCached() {
        _topRateMovieStateFlow.value = NetworkState.Loading
        viewModelScope.launch {
            _topRateMovieStateFlow.value =
                NetworkState.Result(BaseApp.database.topRatingDao().getAllTopRating())
        }
    }
}

class TopRatingRepository @Inject
constructor(private val movieServices: MovieServices) {

 


    suspend fun getTopRateMovie(
        authorization: String,
    ) = movieServices.getTopRated(authorization)


}