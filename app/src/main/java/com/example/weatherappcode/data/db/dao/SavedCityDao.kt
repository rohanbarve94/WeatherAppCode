package com.example.weatherappcode.data.db.dao

import androidx.room.*
import com.example.weatherappcode.data.db.entity.SavedCity

@Dao
interface SavedCityDao {
    @Insert
    suspend fun insert(savedCity: SavedCity): Long

    @Update
    suspend fun update(savedCity: SavedCity)

    @Query("SELECT * FROM saved_city where flag=\"Y\"")
    suspend fun getSavedCity(): List<SavedCity>
}