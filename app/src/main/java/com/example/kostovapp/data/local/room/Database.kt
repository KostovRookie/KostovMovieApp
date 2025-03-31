package com.example.kostovapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kostovapp.data.model.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}