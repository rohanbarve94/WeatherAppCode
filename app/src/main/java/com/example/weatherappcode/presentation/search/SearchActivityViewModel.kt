package com.example.weatherappcode.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappcode.data.db.entity.SavedCity
import com.example.weatherappcode.data.model.citylist.CityListItem
import com.example.weatherappcode.data.repository.SavedCityRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchActivityViewModel(private val repository: SavedCityRepository) : ViewModel(){

    var savedCityList = MutableLiveData<List<SavedCity>>()
    var savedCityListTemp = arrayListOf<SavedCity>()
    var cityObjectList = arrayListOf<CityListItem>()
    var cityListArray = arrayListOf<String>()

    val searchValue = MutableLiveData<String>()

    fun getCityList(jsonFileString:String){
        val gson = GsonBuilder().create()
        cityObjectList = gson.fromJson(jsonFileString, object :
            TypeToken<ArrayList<CityListItem>>() {}.type)
        cityListArray = cityObjectList.map { it.name }.distinct() as ArrayList<String>
    }

    fun getSavedCity(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                savedCityListTemp = repository.getSavedCity() as ArrayList<SavedCity>
            }
            savedCityList.value = savedCityListTemp
        }
    }

    fun removeCity(item: SavedCity){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                item.flag="N"
                repository.removeCity(item)
                savedCityListTemp.remove(item)
            }
            savedCityList.value = savedCityListTemp
        }
    }
}
