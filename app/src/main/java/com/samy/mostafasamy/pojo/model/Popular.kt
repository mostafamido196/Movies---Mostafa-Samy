package com.samy.mostafasamy.pojo.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "PopularList")
data class Popular(
    @PrimaryKey val id: Int,//
    val title: String,//
    val poster_path: String,//img
    val release_date: String,//

)
