package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blueray.kees.databinding.DeliveringRvItemBinding
import com.blueray.kees.model.UnderDelivery

class DeliveringOrdersAdapter(
    var list: List<UnderDelivery>,
    var onClickListener: (id: Int) -> Unit
) :
    RecyclerView.Adapter<DeliveringOrdersAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: DeliveringRvItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            DeliveringRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            val deliveryData = list[position]
            binding.orderIdTv.text = "#"+deliveryData.id.toString()
            binding.locationTv.text = deliveryData.area +","+ deliveryData.address
            binding.priceTv.text = deliveryData.total_price.toString() +" JOD"
            binding.productsCountTv.text = deliveryData.item_count.toString()
        }
        holder.itemView.setOnClickListener {
            onClickListener(list[position].id)
        }

    }
}