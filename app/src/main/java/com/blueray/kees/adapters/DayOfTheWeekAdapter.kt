package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.DayItemBinding

class DayOfTheWeekAdapter(
    // todo change model
    var list: List<String>,
    var onClickListener : (id : String)->Unit
): RecyclerView.Adapter<DayOfTheWeekAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: DayItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = 7

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onClickListener("food")
        }
        holder.apply {
            binding.dayText.text = list[position]
        }
    }

}