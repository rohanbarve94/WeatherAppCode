package com.example.weatherapp.data.model.forecastApi

data class Weather(
    val description: String,
    val icon: String,
    val id: String,
    val main: String
)