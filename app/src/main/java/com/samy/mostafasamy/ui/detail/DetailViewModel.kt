package com.samy.mostafasamy.ui.detail


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.samy.mostafasamy.data.remote.MovieServices
import com.samy.mostafasamy.pojo.model.Detail
import com.samy.mostafasamy.pojo.response.DetailResponse
import com.samy.mostafasamy.ui.base.BaseViewModel
import com.samy.mostafasamy.utils.NetworkState
import com.samy.mostafasamy.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: DetailRepository) :
    BaseViewModel() {

    private val _detailMovieStateFlow = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val detailMovieSateFlow get() = _detailMovieStateFlow


    fun getDetail(key: Int) {
        _detailMovieStateFlow.value = NetworkState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            if (Utils.isInternetAvailable()) {
                Log.d("hamoly", " if (Utils.isInternetAvailable())")
                runApi<DetailResponse, Detail>(
                    _detailMovieStateFlow,
                    repository.getMovie(authorization, key),
                    converter = { data: DetailResponse? -> convertToDetail(data) },
//                    caching = { data: List<Popular> -> cachingPopular(data) },
                )
            }
        }

    }


    private fun convertToDetail(detailResponse: DetailResponse?): Detail {
            return Detail(detailResponse!!.id, detailResponse.title, detailResponse.poster_path, detailResponse.release_date,detailResponse.overview,detailResponse.popularity,detailResponse.vote_average,detailResponse.vote_count
            )
    }

}

class DetailRepository @Inject
constructor(private val movieServices: MovieServices) {

    suspend fun getMovie(
        authorization: String,
        key: Int,
    ) = movieServices.getMovieDetails(key,authorization)


}