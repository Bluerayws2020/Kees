package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.LastOrdersItemBinding

class DriverLastOrdersAdapter (val list: List<String> = listOf(),
                               val onClickListener : (id:Int)->Unit
) :
    RecyclerView.Adapter<DriverLastOrdersAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: LastOrdersItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = LastOrdersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            // todo implement binding Here
        }
        holder.binding.callBtn.setOnClickListener {
            onClickListener(position)
        }
    }
}