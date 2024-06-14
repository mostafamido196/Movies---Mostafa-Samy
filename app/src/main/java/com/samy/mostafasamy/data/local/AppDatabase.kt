package com.samy.mostafasamy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samy.mostafasamy.pojo.model.Popular

@Database(entities = [Popular::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun popularDao(): PopularDao
}