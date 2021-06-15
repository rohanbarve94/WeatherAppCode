package com.example.weatherappcode.data.repository

import com.example.weatherapp.data.api.NetworkService
import com.example.weatherapp.data.model.forecastApi.ForecastResponse
import com.example.weatherapp.data.model.weatherApi.WeatherResponse
import com.example.weatherappcode.BuildConfig
import com.example.weatherappcode.data.db.dao.CachedDataDao
import com.example.weatherappcode.data.db.dao.SavedCityDao
import com.example.weatherappcode.data.db.entity.CachedData
import com.example.weatherappcode.data.db.entity.SavedCity
import retrofit2.Response

class WeatherDetailsRepository(private val dao: SavedCityDao,private val cachedDao:CachedDataDao ,private val networkService: NetworkService) {

    suspend fun getWeatherDetails(city:String): Response<WeatherResponse> {
        return networkService.getCurrentTemp(city,BuildConfig.API_KEY,"metric")
    }

    suspend fun getForecastDetails(city:String): Response<ForecastResponse> {
        return networkService.getForecast(city, BuildConfig.API_KEY,"metric")
    }

    suspend fun addCity(city: String){
        val savedCity = SavedCity(0,city,"Y")
        dao.insert(savedCity)
    }

    suspend fun insertCachedData(cachedData: CachedData){
        cachedDao.insert(cachedData)
    }

    suspend fun deleteCachedData(city: String){
        cachedDao.delete(city)
    }

    suspend fun getCachedData(city:String):List<CachedData>{
        return cachedDao.getCachedData(city)
    }
}