package com.samy.mostafasamy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samy.mostafasamy.pojo.model.Popular
import com.samy.mostafasamy.pojo.model.TopRating

@Database(entities = [Popular::class, TopRating::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun popularDao(): PopularDao
    abstract fun topRatingDao(): TopRatingDao
}