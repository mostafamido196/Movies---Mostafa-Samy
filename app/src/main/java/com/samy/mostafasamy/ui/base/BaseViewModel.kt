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
    public lateinit var sharedHelper: SharedHelper

//    private val userData: User?
//        get() = sharedHelper.userData()

    val authorization: String
        get() = sharedHelper.token

//    fun saveUserData(user: User, token: String) {
//        sharedHelper.setUserData(user, token)
//    }

//    val adminData: AdminLoginResponse.Admin?
//        get() = sharedHelper.adminData()

//    fun saveAdminData(user: AdminLoginResponse.Admin, token: String) {
//        sharedHelper.setAdminData(user, token)
//    }

//    fun saveUserType(type: String) {
//        sharedHelper.saveUserType(type)
//    }
//
//    fun userType(): String = sharedHelper.userType

//    fun isLogged(): Boolean = userData != null

//    fun lang(): String = sharedHelper.lang
//
//    fun isDarkModeEnabled(): Boolean = sharedHelper.isDarkModeEnabled()

//    fun setDarkModeEnabled(is_dark: Boolean) = sharedHelper.setDarkModeEnabled(is_dark)
//
//    fun saveLang(lang: String) = sharedHelper.saveLang(lang)

//    fun signOut() = sharedHelper.signOut()

//    fun startAuth(mContext: Context) {
//        val intent = Intent(mContext, AuthActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        mContext.startActivity(intent)
//    }

    fun <T, V> runApi(
        _apiStateFlow: MutableStateFlow<NetworkState>,
        block: Response<T>,
        converter: ((T) -> V)? = null,
        caching: ((V) -> Unit)? = null,
        getCaching: (suspend () -> Unit)? = null,
    ) {
        Log.d("hamoly","runApi")
        _apiStateFlow.value = NetworkState.Loading
        try {
            Log.d("hamoly","try")
            if (Utils.isInternetAvailable()) {
                Log.d("hamoly","Utils.isInternetAvailable")
                //api
                CoroutineScope(Dispatchers.IO).launch {

                    kotlin.runCatching {
                        block
                    }.onFailure {

                        Log.e(TAG, "runApi: 3")
                        when (it) {
                            is java.net.UnknownHostException ->
                                _apiStateFlow.value =
                                    NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)

                            is java.net.ConnectException ->
                                _apiStateFlow.value =
                                    NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)

                            else -> _apiStateFlow.value =
                                NetworkState.Error(Constants.Codes.UNKNOWN_CODE)
                        }

                    }.onSuccess {
                        Log.e(TAG, "runApi: 4")
                        if (it.body() != null) {
//                            if (converter != null)
//                                _apiStateFlow.value = NetworkState.Result(converter(it.body()!!))
//                            else
                                _apiStateFlow.value = NetworkState.Result(it.body())
//                            if (converter != null && caching != null) caching(converter(it.body()!!))
                        } else {
                            Log.e(TAG, "runApi: ${it.errorBody()}")
                            _apiStateFlow.value = NetworkState.Error(Constants.Codes.UNKNOWN_CODE)
                        }
                    }

                }
//            } else {
               /* //room
                CoroutineScope(Dispatchers.IO).launch {

                    kotlin.runCatching {
                        block
                    }.onFailure {

                        Log.e(TAG, "runApi: 3")
                        when (it) {
                            is java.net.UnknownHostException ->
                                _apiStateFlow.value =
                                    NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)

                            is java.net.ConnectException ->
                                _apiStateFlow.value =
                                    NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)

                            else -> _apiStateFlow.value =
                                NetworkState.Error(Constants.Codes.UNKNOWN_CODE)
                        }

                    }.onSuccess {
                        if (getCaching != null) {
                            _apiStateFlow.value = NetworkState.Result(getCaching())
                        }
                    }
                }
//                _apiStateFlow.value = NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)*/
            }
        } catch (e: Exception) {
            Log.e(TAG, "runApi: ${e.message}")
        }


    }

    companion object {
        private val TAG = this::class.java.name

//        fun signOut(mContext: Context) = SharedHelper(mContext).signOut()

//        fun startAuth(mContext: Context) {
//            val intent = Intent(mContext, AuthActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            mContext.startActivity(intent)
//        }

    }

}