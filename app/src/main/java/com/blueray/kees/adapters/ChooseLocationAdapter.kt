package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.CheckupLocationItemBinding
import com.blueray.kees.model.CustomerGetAddressesData

class ChooseLocationAdapter(
    var list: List<CustomerGetAddressesData>,
    var onItemClick: (pos: Int) -> Unit
) : RecyclerView.Adapter<ChooseLocationAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding: CheckupLocationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CheckupLocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            areaTv.text = data.area
            addressTv.text = data.address
        }
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }
}