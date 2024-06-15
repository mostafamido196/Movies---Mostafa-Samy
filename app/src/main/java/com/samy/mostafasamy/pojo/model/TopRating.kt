package com.samy.mostafasamy.pojo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TopRatingList")
data class TopRating(
    @PrimaryKey val id: Long,//
    val title: String,//
    val poster_path: String,//img
    val release_date: String,//
    val overview: String,//
    val popularity: Double,//
    val vote_average: Double,
    val vote_count: Int

)
