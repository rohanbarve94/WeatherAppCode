package com.example.weatherappcode.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_city")
data class SavedCity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var cityName:String,
    var flag:String
)
