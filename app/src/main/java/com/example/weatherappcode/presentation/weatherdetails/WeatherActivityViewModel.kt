package com.example.weatherappcode.presentation.weatherdetails

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.forecastApi.ForecastResponse
import com.example.weatherapp.data.model.weatherApi.WeatherResponse
import com.example.weatherappcode.data.db.entity.CachedData
import com.example.weatherappcode.data.repository.WeatherDetailsRepository
import com.example.weatherappcode.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round


class WeatherActivityViewModel(private val repository: WeatherDetailsRepository) : ViewModel() {
    var currentTemp = MutableLiveData<String>()
    var currentCity = MutableLiveData<String>()
    var currentDate = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var dateTimeOne = MutableLiveData<String>()
    var descriptionOne = MutableLiveData<String>()
    var tempOne = MutableLiveData<String>()
    var dateTimeTwo = MutableLiveData<String>()
    var descriptionTwo = MutableLiveData<String>()
    var tempTwo = MutableLiveData<String>()
    var dateTimeThree = MutableLiveData<String>()
    var descriptionThree = MutableLiveData<String>()
    var tempThree = MutableLiveData<String>()

    private var statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() {
            return statusMessage
        }

    @SuppressLint("SimpleDateFormat")
    fun getWeatherForecastDetails(city: String){
        viewModelScope.launch {
            lateinit var weatherResponse: Response<WeatherResponse>
            lateinit var forecastResponse: Response<ForecastResponse>
            var errorFlag="0"
            withContext(Dispatchers.IO) {
                try{
                    weatherResponse = repository.getWeatherDetails(city)
                    forecastResponse = repository.getForecastDetails(city)

                    if(weatherResponse.code().toString()=="404"){
                        errorFlag="3"
                    }
                }catch (e:UnknownHostException){
                    errorFlag="2"
                }catch (e: Exception){
                    errorFlag="1"
                }
            }

            if (errorFlag=="0" && weatherResponse.isSuccessful) {
                if (forecastResponse.isSuccessful && weatherResponse.body() != null && weatherResponse.body()?.main != null) {

                    var temp = weatherResponse.body()?.main?.temp
                    temp = round(temp!!.toDouble()).toInt().toString()
                    currentTemp.value = "$temp\u00B0"
                    currentCity.value = weatherResponse.body()?.name.toString()
                    description.value = weatherResponse.body()?.weather?.get(0)?.description

                    val sdf = SimpleDateFormat("dd MMM , yyyy HH:mm")
                    val c: Calendar = Calendar.getInstance()
                    c.setTime(Date())
                    currentDate.value = sdf.format(c.getTime())

                    if (forecastResponse.body() != null && forecastResponse.body()?.list != null) {
                        for(i in 1..4){
                            when (i) {
                                1 -> {
                                    dateTimeOne.value =
                                        forecastResponse.body()?.list?.get(0)?.dt_txt
                                    descriptionOne.value =
                                        forecastResponse.body()?.list?.get(0)?.weather?.get(
                                            0
                                        )?.description
                                    var tempVal = forecastResponse.body()?.list?.get(0)?.main?.temp
                                    tempVal = round(tempVal!!.toDouble()).toInt().toString()
                                    tempOne.value = "$tempVal\u00B0"
                                }
                                2 -> {
                                    dateTimeTwo.value =
                                        forecastResponse.body()?.list?.get(1)?.dt_txt
                                    descriptionTwo.value =
                                        forecastResponse.body()?.list?.get(0)?.weather?.get(
                                            0
                                        )?.description
                                    var tempVal = forecastResponse.body()?.list?.get(0)?.main?.temp
                                    tempVal = round(tempVal!!.toDouble()).toInt().toString()
                                    tempTwo.value = "$tempVal\u00B0"
                                }
                                3 -> {
                                    dateTimeThree.value =
                                        forecastResponse.body()?.list?.get(2)?.dt_txt
                                    descriptionThree.value =
                                        forecastResponse.body()?.list?.get(0)?.weather?.get(
                                            0
                                        )?.description
                                    var tempVal = forecastResponse.body()?.list?.get(0)?.main?.temp
                                    tempVal = round(tempVal!!.toDouble()).toInt().toString()
                                    tempThree.value = "$tempVal\u00B0"
                                }
                            }
                        }

                        repository.deleteCachedData(city)
                        val cachedData = CachedData(0,city,currentTemp.value.toString(),"T1",description.value.toString(),currentDate.value.toString(),"Y")
                        repository.insertCachedData(cachedData)
                        val cachedDataOne = CachedData(0,city,tempOne.value.toString(),"T2",descriptionOne.value.toString(),dateTimeOne.value.toString(),"Y")
                        repository.insertCachedData(cachedDataOne)
                        val cachedDataTwo = CachedData(0,city,tempTwo.value.toString(),"T3",descriptionTwo.value.toString(),dateTimeTwo.value.toString(),"Y")
                        repository.insertCachedData(cachedDataTwo)
                        val cachedDataThree = CachedData(0,city,tempThree.value.toString(),"T4",descriptionThree.value.toString(),dateTimeThree.value.toString(),"Y")
                        repository.insertCachedData(cachedDataThree)

                        statusMessage.value = Event("Success")
                    } else {
                        loadCachedData(city,errorFlag)
                    }
                } else {
                    loadCachedData(city,errorFlag)
                }
            } else {
                loadCachedData(city,errorFlag)
            }
        }
    }

    fun loadCachedData(city: String,errorFlag:String){
        viewModelScope.launch {
            var cachedList: List<CachedData>
            withContext(Dispatchers.IO){
                cachedList = repository.getCachedData(city)
            }

            if(errorFlag=="2"){
                statusMessage.value = Event("Net Error")
            }else if(errorFlag=="3"){
                statusMessage.value = Event("City not found")
            }else{
                statusMessage.value = Event("Other Error")
            }

            if(cachedList.isNotEmpty()){
                for(item in cachedList){
                    when (item.type) {
                        "T1" -> {
                            currentTemp.value = item.temp
                            description.value = item.description
                            currentDate.value = item.dateVal
                            currentCity.value = city
                        }
                        "T2" -> {
                            tempOne.value = item.temp
                            descriptionOne.value = item.description
                            dateTimeOne.value = item.dateVal
                        }
                        "T3" -> {
                            tempTwo.value = item.temp
                            descriptionTwo.value = item.description
                            dateTimeTwo.value = item.dateVal
                        }
                        "T4" -> {
                            tempThree.value = item.temp
                            descriptionThree.value = item.description
                            dateTimeThree.value = item.dateVal
                        }
                    }
                }
                statusMessage.value = Event("Data Cached")
            }else{
                statusMessage.value = Event("Error")
            }
        }
    }

    fun addCity(city: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.addCity(city)
            }
            statusMessage.value = Event("City Added")
        }
    }
}