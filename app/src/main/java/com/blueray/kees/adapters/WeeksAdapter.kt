package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.R
import com.blueray.kees.databinding.DayItemBinding
import com.blueray.kees.model.Days
import com.blueray.kees.model.WeeklyBasketData

class WeeksAdapter (
    var list: List<WeeklyBasketData>,
    var onClickListener: (data :WeeklyBasketData, position: Int) -> Unit
) : RecyclerView.Adapter<WeeksAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: DayItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.itemView.setOnClickListener {
            onClickListener(data,position)
        }
        holder.apply {
            binding.dayText.text = binding.root.context.getString(R.string.week) +" "+list[position].week_number

            if (list[position].selected) {
                (binding.root as CardView).background.setTint(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.purpleColor
                    )
                )
                binding.dayText.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.white
                    )
                )
            } else {
                (binding.root as CardView).background.setTint(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.white
                    )
                )
                binding.dayText.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.dark_grey
                    )
                )
            }
        }
    }
}