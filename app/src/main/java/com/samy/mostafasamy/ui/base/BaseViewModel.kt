package com.samy.mostafasamy.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel
import com.samy.mostafasamy.utils.Constants
import com.samy.mostafasamy.utils.NetworkState
import com.samy.mostafasamy.utils.SharedHelper
import com.samy.mostafasamy.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() :
    ViewModel() {

    @Inject
    lateinit var sharedHelper: SharedHelper


    val authorization: String
        get() = sharedHelper.token


    fun <T, V> runApi(
        _apiStateFlow: MutableStateFlow<NetworkState>,
        block: Response<T>,
        converter: ((T) -> List<V>)? = null,
        caching: ((List<V>) -> Unit)? = null,
//        getCaching: (suspend () -> Unit)? = null,
    ) {
        Log.d("hamoly", "runApi")
        _apiStateFlow.value = NetworkState.Loading
        try {
            Log.d("hamoly", "try")
            if (Utils.isInternetAvailable()) {
                Log.d("hamoly", "Utils.isInternetAvailable")
                //api
                CoroutineScope(Dispatchers.IO).launch {

                    kotlin.runCatching {
                        block
                    }.onFailure {
                        Log.e(TAG, "runApi: 3:it.message: ${it.message}")
                    }.onSuccess {
                        Log.e(TAG, "runApi: 4")
                        if (it.body() != null) {
                            if (converter != null)
                                _apiStateFlow.value = NetworkState.Result(converter(it.body()!!))
                            else
                                _apiStateFlow.value = NetworkState.Result(it.body())
                            if (caching != null && converter != null)
                                    caching(converter(it.body()!!))
                        } else {
                            Log.e(TAG, "runApi: ${it.errorBody()}")
                            _apiStateFlow.value = NetworkState.Error(Constants.Codes.UNKNOWN_CODE)
                        }
                    }
                }
            } /*else {
                if (getCaching != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        getCaching()
                    }
                }


            }*/
        } catch (e: Exception) {
            Log.e(TAG, "runApi: ${e.message}")
        }


    }

    companion object {
        private val TAG = this::class.java.name
    }

}