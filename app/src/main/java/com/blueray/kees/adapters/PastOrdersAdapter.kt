package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.FinishedOrdersItemBinding
import com.blueray.kees.model.PastOrderData

class PastOrdersAdapter(
    var list: List<PastOrderData> ,
    var onItemClick: (id:String) -> Unit
) : RecyclerView.Adapter<PastOrdersAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            FinishedOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            dateTv.text = data.date
            locationTv.text = data.area + "," + data.address
            timeTv.text = data.time
            orderIdTv.text = "#" + data.id
            ClintNameTv.text = data.driver_name

        }
        holder.itemView.setOnClickListener {
            onItemClick(data.id.toString())
        }
    }

    override fun getItemCount(): Int = list.size

    inner class MyViewHolder(var binding: FinishedOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}