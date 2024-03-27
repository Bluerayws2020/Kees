package com.blueray.kees.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.R
import com.blueray.kees.databinding.CheckOutItemBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.WeeklyBasketData

class CheckOutWeekAdapter(
    var list: List<WeeklyBasketData>,
    var onClick: (data: WeeklyBasketData , position: Int) -> Unit
) : RecyclerView.Adapter<CheckOutWeekAdapter.MyViewHolder>() {

    private var filteredList: List<WeeklyBasketData> =
        list.filter { it.weekly_basket_products.isNotEmpty() }

    inner class MyViewHolder(var binding: CheckOutItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CheckOutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = filteredList[position]
        holder.apply {
            binding.weekNameTv.text =
                binding.root.context.getString(R.string.week) + " " + data.week_number
            binding.price.text = data.total_price.toString() + " JOD"
            binding.paidTv.text = data.payment_status
            if (list[position].selected && data.payment_status != "Paid") {
                (binding.root as CardView).setBackgroundResource(R.drawable.card_view_background)

            } else {
                (binding.root as CardView).setBackgroundResource(R.drawable.unselected_card_view_background)

            }
        }
        holder.itemView.setOnClickListener {
            if (data.payment_status != "Paid" && data.payment_status != "مدفوع"){
                onClick(data , position)
            }

        }
    }

    fun setData(newList: List<WeeklyBasketData>) {
        list = newList
        filteredList = list.filter { it.weekly_basket_products.isNotEmpty() }
        notifyDataSetChanged()
    }
}
