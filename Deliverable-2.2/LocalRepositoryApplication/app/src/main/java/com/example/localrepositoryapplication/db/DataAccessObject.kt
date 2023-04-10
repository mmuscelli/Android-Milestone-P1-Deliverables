package com.example.localrepositoryapplication.db

import androidx.room.*

@Dao
interface MainDao {
    // Insert new data to Entity Student.
    @Insert
    fun insertNewStudent(vararg newStudent: Student)

    // Update existing data in Entity Student.
    @Update
    fun updateStudent(vararg student: Student)

    // Delete existing data from Entity Student.
    @Delete
    fun deleteStudent(vararg student: Student)

    // Query Entity Student; Get all student data in Entity Student.
    @Query("SELECT * FROM STUDENT ORDER BY FIRST_NAME ASC")
    fun getAllStudent(): List<Student>

    // Query Entity Student; Get student data whose first name is name.
    @Query("SELECT * FROM STUDENT WHERE puid = :targetPuid")
    fun getStudent(targetPuid: String): Student
}