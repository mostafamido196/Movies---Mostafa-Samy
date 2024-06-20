package com.samy.mostafasamy.ui.search

import android.text.Editable
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.samy.mostafasamy.ui.base.BaseViewModel
import com.samy.mostafasamy.data.remote.MovieServices
import com.samy.mostafasamy.di.BaseApp
import com.samy.mostafasamy.pojo.model.Popular
import com.samy.mostafasamy.pojo.model.Search
import com.samy.mostafasamy.pojo.response.PopularResponse
import com.samy.mostafasamy.pojo.response.SearchResponse
import com.samy.mostafasamy.utils.NetworkState
import com.samy.mostafasamy.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) :
    BaseViewModel() {

    private val _searchMovieStateFlow = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val searchMovieSateFlow get() = _searchMovieStateFlow




    fun search(text: String) {
        _searchMovieStateFlow.value = NetworkState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            if (Utils.isInternetAvailable()) {
                Log.d("hamoly", " if (Utils.isInternetAvailable())")
                runApi<SearchResponse?, List<Search>>(
                    _searchMovieStateFlow,
                    repository.getMovie(authorization,text),
                    converter = { data: SearchResponse? -> convertToPopular(data) },
//                    caching = { data: List<Popular> -> cachingPopular(data) },
                )
            }else{
                _searchMovieStateFlow.value = NetworkState.Error(1,"Check Internet Connection")
            }
        }

    }



    private fun convertToPopular(popularResponse: SearchResponse?): List<Search> {
        val movies = ArrayList<Search>()
        popularResponse?.results?.forEach { movie ->
            movies.add(Search(movie.id, movie.title, movie.poster_path, movie.release_date,movie.overview,movie.popularity,movie.vote_average,movie.vote_count ))
        }
        return movies
    }

}

class SearchRepository @Inject
constructor(private val movieServices: MovieServices) {

    suspend fun getMovie(
        authorization: String,
        key:String
    ) = movieServices.searchMovies(authorization,key)



}