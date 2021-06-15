package com.example.weatherappcode.data.repository

import com.example.weatherappcode.data.db.dao.SavedCityDao
import com.example.weatherappcode.data.db.entity.SavedCity

class SavedCityRepository(private val dao: SavedCityDao) {

    suspend fun getSavedCity():List<SavedCity>{
        return dao.getSavedCity()
    }

    suspend fun removeCity(item:SavedCity){
        dao.update(item)
    }
}