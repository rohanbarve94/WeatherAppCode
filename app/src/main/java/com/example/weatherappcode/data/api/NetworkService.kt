package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.forecastApi.ForecastResponse
import com.example.weatherapp.data.model.weatherApi.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("weather")
    suspend fun getCurrentTemp(
        @Query("q") q: String,@Query("appid") appid: String,@Query("units") units: String
    ): Response<WeatherResponse>

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") q: String,@Query("appid") appid: String,@Query("units") units: String
    ): Response<ForecastResponse>
}