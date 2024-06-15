package com.samy.mostafasamy.pojo.model

data class Detail(
    var id: Int,//
    var title: String,//
    var poster_path: String,//img
    var release_date: String,//
    var overview: String,//
    var popularity: Double,//
    val vote_average: Double,
    val vote_count: Int

    )
