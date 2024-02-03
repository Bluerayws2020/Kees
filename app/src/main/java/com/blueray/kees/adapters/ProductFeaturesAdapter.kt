package com.blueray.kees.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.ProductFeaturesItemBinding
import com.blueray.kees.model.Features

class ProductFeaturesAdapter (
    var list: List<Features>,
): RecyclerView.Adapter<ProductFeaturesAdapter.MyViewHolder>() {

    val selectedItems = mutableListOf<String>()
    inner class MyViewHolder(var binding: ProductFeaturesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ProductFeaturesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val adapter = FeaturesSpinnerAdapter(context =holder.itemView.context, list[position].options)
        holder.apply {
            binding.label.text = list[position].name
            binding.spinner.adapter = adapter
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedPosition: Int, p3: Long) {
                    if(!selectedItems.contains(list[position].options[selectedPosition].id.toString())){
                        for (item in list[position].options){
                            if(selectedItems.contains(item.id.toString())){
                                selectedItems.remove(item.id.toString())
                            }
                        }
                        selectedItems.add(list[position].options[selectedPosition].id.toString())
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // nothing
                }

            }
        }
    }
}