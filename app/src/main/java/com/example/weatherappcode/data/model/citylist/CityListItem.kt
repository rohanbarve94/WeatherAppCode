package com.example.weatherappcode.data.model.citylist

data class CityListItem(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val state: String
)