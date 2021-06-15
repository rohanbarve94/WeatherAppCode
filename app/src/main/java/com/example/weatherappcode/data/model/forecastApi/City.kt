package com.example.weatherapp.data.model.forecastApi

data class City(
    val coord: Coord,
    val country: String,
    val id: String,
    val name: String,
    val population: String,
    val sunrise: String,
    val sunset: String,
    val timezone: String
)