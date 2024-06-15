package com.samy.mostafasamy.pojo.model

import androidx.room.PrimaryKey

data class Search(
    @PrimaryKey
    val id: Long,//
    val title: String,//
    val poster_path: String,//img
    val release_date: String,//
    val overview: String,//
    val popularity: Double,//
    val vote_average: Double,
    val vote_count: Int
)