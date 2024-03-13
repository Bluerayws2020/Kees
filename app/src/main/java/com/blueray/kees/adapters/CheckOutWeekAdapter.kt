package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.R
import com.blueray.kees.databinding.CheckOutItemBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.WeeklyBasketData

class CheckOutWeekAdapter(
    var list: List<WeeklyBasketData>,
    var onClick: (data: WeeklyBasketData) -> Unit
) : RecyclerView.Adapter<CheckOutWeekAdapter.MyViewHolder>() {

    private var filteredList: List<WeeklyBasketData> = list.filter { it.weekly_basket_products.isNotEmpty() }

    inner class MyViewHolder(var binding: CheckOutItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CheckOutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = filteredList[position]
        holder.apply {
            binding.weekNameTv.text = data.week_number + binding.root.context.getString(R.string.week)
            binding.price.text = data.total_price.toString()
        }
        holder.itemView.setOnClickListener {
            onClick(data)
        }
    }

    fun setData(newList: List<WeeklyBasketData>) {
        list = newList
        filteredList = list.filter { it.weekly_basket_products.isNotEmpty() }
        notifyDataSetChanged()
    }
}
