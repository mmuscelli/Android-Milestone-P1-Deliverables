package com.example.localrepositoryapplication.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "STUDENT")
data class Student(
    @PrimaryKey val puid: String,
    @ColumnInfo(name = "FIRST_NAME") val firstName: String?,
    @ColumnInfo(name = "LAST_NAME") val lastName: String?
)