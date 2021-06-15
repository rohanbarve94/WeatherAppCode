package com.example.weatherappcode.presentation.weatherdetails

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappcode.R
import com.example.weatherappcode.data.api.RetrofitClient
import com.example.weatherappcode.data.db.AppDB
import com.example.weatherappcode.data.repository.SavedCityRepository
import com.example.weatherappcode.data.repository.WeatherDetailsRepository
import com.example.weatherappcode.databinding.ActivityWeatherBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class WeatherActivity: AppCompatActivity(){

    lateinit var binding: ActivityWeatherBinding
    lateinit var viewModel: WeatherActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_weather
        )

        val dao = AppDB.getInstance(application, CoroutineScope(Dispatchers.IO)).savedCityDao()
        val cachedDao = AppDB.getInstance(application, CoroutineScope(Dispatchers.IO)).cachedDataDao()
        val networkService = RetrofitClient().apiInterface
        val repository = WeatherDetailsRepository(dao,cachedDao,networkService)
        val factory = WeatherActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(WeatherActivityViewModel::class.java)
        binding.weatherActivityViewModel = viewModel
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }

        val city=intent.getStringExtra("city")

        binding.progressLayout.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        binding.rootLayout.visibility = View.GONE
        viewModel.getWeatherForecastDetails(city!!)

        /** Action as per Network Response **/
        viewModel.message.observe(this, {
            it.getContentIfNotHandled()?.let { content ->
                when (content) {
                    "Success" -> {
                        binding.progressLayout.visibility = View.GONE
                        binding.rootLayout.visibility = View.VISIBLE
                        binding.errorLayout.visibility = View.GONE
                    }
                    "Error" -> {
                        binding.progressLayout.visibility = View.GONE
                        binding.rootLayout.visibility = View.GONE
                        binding.errorLayout.visibility = View.VISIBLE
                    }
                    "City Added" -> {
                        Snackbar.make(binding.root, "City added to favorite list", Snackbar.LENGTH_LONG).show()
                    }
                    "Net Error" -> {
                        binding.errorText.text = getString(R.string.internet_error_msg)
                    }
                    "Other Error" -> {
                        binding.errorText.text = getString(R.string.something_went_wrong)
                    }
                    "City not found" -> {
                        binding.errorText.text = getString(R.string.error_city)
                    }
                    "Data Cached" -> {
                        binding.progressLayout.visibility = View.GONE
                        binding.rootLayout.visibility = View.VISIBLE
                        binding.errorLayout.visibility = View.GONE
                        Snackbar.make(binding.root, "Internet Issue!", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })

        /** Retry button click Action **/
        binding.retry.setOnClickListener {
            binding.progressLayout.visibility = View.VISIBLE
            binding.rootLayout.visibility = View.GONE
            binding.errorLayout.visibility = View.GONE
            viewModel.getWeatherForecastDetails(city!!)
        }

        binding.addFavorite.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to add this city to favorite list?")
                .setPositiveButton("Yes") { d, _ ->
                    viewModel.addCity(city!!)
                    Snackbar.make(binding.root, "Item Added!", Snackbar.LENGTH_LONG).show()
                    d.dismiss()
                }
                .setNegativeButton("No") { d, _ ->
                    d.cancel()
                }
                .create()
                .show()
        }

    }
}