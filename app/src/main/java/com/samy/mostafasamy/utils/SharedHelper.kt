package com.samy.mostafasamy.utils

import android.content.Context
import com.google.gson.Gson
import com.samy.mostafasamy.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class SharedHelper @Inject constructor(@ApplicationContext mContext: Context) {
//
//    private val userTypeSharedPreferences =
//        mContext.getSharedPreferences(Constants.ShardKeys.USER_TYPE_NAME, Context.MODE_PRIVATE)
//    private val userTypeEditor = userTypeSharedPreferences.edit()
//
//    val userType: String
//        get() = userTypeSharedPreferences.getString(
//            Constants.ShardKeys.TYPE,
//            Constants.USER
//        )!!
//
//    fun saveUserType(type: String) {
//        userTypeEditor.putString(Constants.ShardKeys.TYPE, type)
//        userTypeEditor.apply()
//    }
//
//    private val sharedPreferences =
//        mContext.getSharedPreferences(Constants.ShardKeys.SHARD_NAME, Context.MODE_PRIVATE)
//    private val editor = sharedPreferences.edit()
//
//
//    val lang: String
//        get() = sharedPreferences.getString(
//            Constants.ShardKeys.LANGUAGE,
//            Locale.getDefault().displayLanguage
//        )!!
//
//    private val splash: Boolean
//        get() = sharedPreferences.getBoolean(
//            Constants.ShardKeys.SPLASH,
//            false
//        )
//
    val token: String
        get() = Constants.Authorization
//
////    fun userData(): User? = Gson().fromJson(
////        sharedPreferences.getString(Constants.ShardKeys.DATA, null),
////        User::class.java
////    )
//
//
////    fun setUserData(value: User, token: String) {
////        val data = Gson().toJson(value, User::class.java)
////        editor.putString(Constants.ShardKeys.DATA, data)
////        editor.putString(Constants.ShardKeys.TOKEN, token)
////        editor.apply()
////    }
//
////    fun adminData(): AdminLoginResponse.Admin? = Gson().fromJson(
////        sharedPreferences.getString(Constants.ShardKeys.DATA, null),
////        AdminLoginResponse.Admin::class.java
////    )
//
////    fun setAdminData(value: AdminLoginResponse.Admin, token: String) {
////        val data = Gson().toJson(value, AdminLoginResponse.Admin::class.java)
////        editor.putString(Constants.ShardKeys.DATA, data)
////        editor.putString(Constants.ShardKeys.TOKEN, token)
////        editor.apply()
////    }
//
//    fun saveSplash() {
//        editor.putBoolean(Constants.ShardKeys.SPLASH, true)
//        editor.apply()
//    }
//
//    fun isSplashed(): Boolean = splash
//
//    fun signOut() {
//        editor.clear()!!.apply()
//    }
//
//    fun isDarkModeEnabled(): Boolean =
//        sharedPreferences.getBoolean(Constants.ShardKeys.DARK_MODE, false)
//
//    fun setDarkModeEnabled(is_dark: Boolean) {
//        editor.putBoolean(Constants.ShardKeys.DARK_MODE, is_dark)
//        editor.apply()
//    }
//
//    fun saveLang(lang: String) {
//        editor.putString(Constants.ShardKeys.LANGUAGE, lang)
//        editor.apply()
//    }
//
//    companion object {
////        private val TAG = this::class.java.name
//    }
//
//    enum class Languages(val lang: String) {
//        EN("en"), AR("ar")
//    }
}