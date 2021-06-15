package com.example.weatherappcode.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_data")
data class CachedData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var city: String,
    var temp: String,
    var type:String,
    var description:String,
    var dateVal:String,
    var flag:String
)
