package com.samy.mostafasamy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samy.mostafasamy.pojo.model.Popular
import com.samy.mostafasamy.pojo.model.TopRating

@Dao
interface TopRatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopRating(topRating: TopRating)

    @Query("SELECT * FROM topratinglist")
    suspend fun getAllTopRating(): List<TopRating>
}