package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.FinishedOrdersItemBinding
import com.blueray.kees.model.FinishedOrderData

class FinishedOrdersAdapter(
    var list: List<FinishedOrderData>
) : RecyclerView.Adapter<FinishedOrdersAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding: FinishedOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            FinishedOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            locationTv.text = data.area + "," + data.address
            ClintNameTv.text = data.user_name
            dateTv.text = data.date
            timeTv.text = data.time
            orderIdTv.text = "#" + data.id
        }
    }

    override fun getItemCount(): Int = list.size
}