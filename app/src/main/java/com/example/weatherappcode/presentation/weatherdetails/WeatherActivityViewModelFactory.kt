package com.example.weatherappcode.presentation.weatherdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappcode.data.repository.SavedCityRepository
import com.example.weatherappcode.data.repository.WeatherDetailsRepository
import com.example.weatherappcode.presentation.search.SearchActivityViewModel

class WeatherActivityViewModelFactory(private val repository: WeatherDetailsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherActivityViewModel::class.java)) {
            return WeatherActivityViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }
}