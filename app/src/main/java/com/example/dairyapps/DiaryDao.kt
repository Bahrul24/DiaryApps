package com.example.dairyapps

import androidx.room.*

@Dao
interface DiaryDao {
    @Query("SELECT * FROM Diary")
    fun getAll(): List<Diary>

    @Insert
    fun insert(vararg diary: Diary)

    @Update
    fun update(diary: Diary)

    @Delete
    fun delete(diary: Diary)
}