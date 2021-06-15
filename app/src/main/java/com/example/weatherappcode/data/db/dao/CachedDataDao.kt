package com.example.weatherappcode.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.weatherappcode.data.db.entity.CachedData

@Dao
interface CachedDataDao {
    @Insert
    suspend fun insert(cachedData: CachedData): Long

    @Query("DELETE FROM cached_data where city=:city")
    suspend fun delete(city:String)

    @Query("SELECT * FROM cached_data where city=:city")
    suspend fun getCachedData(city:String): List<CachedData>
}