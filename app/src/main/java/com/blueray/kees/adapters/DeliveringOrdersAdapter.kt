package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blueray.kees.databinding.DeliveringRvItemBinding

class DeliveringOrdersAdapter(val list: List<String> = listOf(),
    val onClickListener : (id:Int)->Unit
) :
    RecyclerView.Adapter<DeliveringOrdersAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: DeliveringRvItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DeliveringRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            // todo implement binding Here
        }
        holder.itemView.setOnClickListener {
            onClickListener(position)
        }
    }
}