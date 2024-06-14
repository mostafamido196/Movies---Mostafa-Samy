package com.samy.mostafasamy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samy.mostafasamy.pojo.model.Popular

@Dao
interface PopularDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopular(popular: Popular)

    @Query("SELECT * FROM popularlist")
    suspend fun getAllPopular(): List<Popular>
}