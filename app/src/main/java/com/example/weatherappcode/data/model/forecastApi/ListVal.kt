package com.example.weatherapp.data.model.forecastApi

import kotlin.collections.List

data class ListVal(
    val clouds: Clouds,
    val dt: String,
    val dt_txt: String,
    val main: Main,
    val pop: String,
    val rain: Rain,
    val sys: Sys,
    val visibility: String,
    val weather: List<Weather>,
    val wind: Wind
)