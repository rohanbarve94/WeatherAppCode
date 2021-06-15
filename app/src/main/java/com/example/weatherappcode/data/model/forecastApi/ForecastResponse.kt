package com.example.weatherapp.data.model.forecastApi

import kotlin.collections.List

data class ForecastResponse(
    val city: City,
    val cnt: String,
    val cod: String,
    val list: List<ListVal>,
    val message: String
)