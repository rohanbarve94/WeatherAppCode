package com.example.weatherappcode.presentation.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappcode.R
import com.example.weatherappcode.data.db.entity.SavedCity
import com.example.weatherappcode.databinding.SavedCityItemBinding
import java.util.*

class SavedCityAdapter (private val savedCityList: ArrayList<SavedCity>,private val clickListener: (SavedCity,Int,String) -> Unit): RecyclerView.Adapter<SavedCityAdapter.SavedCityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: SavedCityItemBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.saved_city_item,
            parent,
            false
        )
        return SavedCityViewHolder(binding, parent.context)
    }


    override fun onBindViewHolder(holder: SavedCityViewHolder, position: Int) {
        val currentItem = savedCityList[position]
        holder.binding.sno.text = (position+1).toString()
        holder.binding.savedCity = currentItem

        holder.binding.remove.setOnClickListener {
            clickListener(currentItem,position,"Remove")
        }

        holder.binding.cityName.setOnClickListener {
            clickListener(currentItem,position,"City")
        }
    }

    override fun getItemCount(): Int {
        return savedCityList.size
    }

    inner class SavedCityViewHolder(val binding: SavedCityItemBinding, val context: Context) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
    }
}
