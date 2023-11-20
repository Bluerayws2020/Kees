package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.HomeSliderItemBinding

class HomeSliderAdapter (
    // todo change model
        var list: List<String>
        ):RecyclerView.Adapter<HomeSliderAdapter.MyViewHolder>() {

        inner class MyViewHolder(var binding :HomeSliderItemBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val binding = HomeSliderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return MyViewHolder(binding)
        }

        override fun getItemCount(): Int = 8

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//                TODO("Not yet implemented")
        }


}