package com.example.weatherappcode.presentation.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappcode.R
import com.example.weatherappcode.data.db.AppDB
import com.example.weatherappcode.data.db.entity.SavedCity
import com.example.weatherappcode.data.repository.SavedCityRepository
import com.example.weatherappcode.databinding.ActivitySearchBinding
import com.example.weatherappcode.presentation.weatherdetails.WeatherActivity
import com.example.weatherappcode.utils.getJsonFromAssets
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SearchActivity : AppCompatActivity(){

    lateinit var binding: ActivitySearchBinding
    lateinit var viewModel: SearchActivityViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: SavedCityAdapter
    private var savedCityList = ArrayList<SavedCity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_search
        )

        val dao = AppDB.getInstance(application, CoroutineScope(Dispatchers.IO)).savedCityDao()
        val repository = SavedCityRepository(dao)
        val factory = SearchActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SearchActivityViewModel::class.java)
        binding.searchActivityViewModel = viewModel
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)

        initData()
        initRecyclerView()
    }

    private fun initData(){
        val jsonFileString: String =
            getJsonFromAssets(this, "city.json")
        viewModel.getCityList(jsonFileString)
        val cityListAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,viewModel.cityListArray)
        binding.search.setAdapter(cityListAdapter)
    }

    private fun initRecyclerView(){
        recyclerView = binding.savedCityList
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = SavedCityAdapter(
            savedCityList
        ) { selectedItem: SavedCity, position: Int, type:String -> itemClick(selectedItem, position,type) }
        recyclerView.adapter = adapter
    }

    private fun initRecyclerViewData(){
        viewModel.getSavedCity()
        viewModel.savedCityList.observe(this, {
            if (it != null && it.isNotEmpty()) {
                binding.favoriteListLabel.visibility = View.VISIBLE
                savedCityList.clear()
                savedCityList.addAll(it)
                adapter.notifyDataSetChanged()
            }else{
                savedCityList.clear()
                adapter.notifyDataSetChanged()
                binding.favoriteListLabel.visibility = View.GONE
            }
        })
    }

    private fun itemClick(item: SavedCity, position: Int , type:String) {

        if(type=="Remove"){
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to remove this city?")
                .setPositiveButton("Yes") { d, _ ->
                    viewModel.removeCity(item)
                    adapter.notifyItemRemoved(position)
                    Snackbar.make(binding.root, "Item Removed!", Snackbar.LENGTH_LONG).show()
                    d.dismiss()
                }
                .setNegativeButton("No") { d, _ ->
                    d.cancel()
                }
                .create()
                .show()
        }else{
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("city",item.cityName)
            startActivity(intent)
        }

    }

    fun checkWeather(view: View){
        if(binding.search.text.toString().isBlank() || binding.search.text.toString()=="null" ){
            Snackbar.make(binding.root, "City cannot be blank", Snackbar.LENGTH_LONG).show()
        }else{
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("city",binding.search.text.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        initRecyclerViewData()
    }

}