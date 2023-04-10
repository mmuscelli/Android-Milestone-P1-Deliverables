package com.example.localrepositoryapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student::class], version = 1)
abstract class MainDatabase : RoomDatabase() {
    // Related Data Access object
    abstract fun mainDao(): MainDao

    companion object {
        private var instance: MainDatabase? = null

        // Function that returns instance of MainDatabase object (Singleton Pattern)
        fun getInstance(context: Context): MainDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context, MainDatabase::class.java, "main_database"
                ).build()
            }
            return instance!!
        }
    }
}