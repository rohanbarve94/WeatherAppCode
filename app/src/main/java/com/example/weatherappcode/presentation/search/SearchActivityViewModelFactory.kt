package com.example.weatherappcode.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappcode.data.repository.SavedCityRepository

class SearchActivityViewModelFactory(private val repository: SavedCityRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchActivityViewModel::class.java)) {
            return SearchActivityViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }
}