package com.samy.mostafasamy.utils

import android.graphics.Movie
import java.security.AccessController.getContext


object Constants {

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val Authorization = "1dfa6341ba4c0536e83d8405985afa6a"
    const val POSTER_URL: String = "https://image.tmdb.org/t/p/w500/"
//    protected fun bind(movie: Movie) {
//        Glide.with(getContext())
//            .load(HttpClientModule.POSTER_URL + movie.getPosterPath())
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(mPosterIV)
//        title_tv.setText(movie.getTitle())
//    }
    const val ContractAddress: String = "contractAddress"

    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val Shortcut: String = "shortcut"
    const val DATA = "data"
    const val SECTION = "section"
    const val BRANCH = "branch"
    const val ADMIN = "admin"
    const val USER = "user"
    const val PROFILE = "profile"
    const val SEARCH_KEYWORD = "search_keyword"
    const val TASK = "task"
    const val MOVIE_ID = "movie_id"
    const val POPULARITY = "popularity"
    const val OVERVIEW = "overview"
    const val RELEASE_DATE = "release_date"
    const val POSTER_PATH = "poster_path"
    const val TITLE = "title"
    const val VOTE_AVERAGE = "vote_average"
    const val VOTE_COUNT = "vote_count"

    object ApisKeys {
        const val BEARER = "Bearer"
    }

    object ShardKeys {

        const val SHARD_NAME = "Develoctiy Tasks"
        const val USER_TYPE_NAME = "User Type"
        const val DATA = "data"
        const val TOKEN = "token"
        const val DARK_MODE = "mode"
        const val LANGUAGE = "lang"
        const val SPLASH = "splash"
        const val TYPE = "type"

    }

    object Codes {
        const val EXCEPTIONS_CODE = 3421
        const val API_KEY_CODE = 5020
        const val AUTH_CODE = 5000
        const val UNKNOWN_CODE = 560
        const val SUCCESSES_CODE = 200
    }

    enum class ComplaintsStates(val value: String) {
        Responded("responed"), Rejected("rejected"),Waiting("waiting")

    }
}