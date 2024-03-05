package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.WitingForDeliveryRvItemBinding
import com.blueray.kees.model.WaitingForDelivery

class WaitingForDeliveryAdapter(
    var list: List<WaitingForDelivery>,
    var onClickListener: (id: Int) -> Unit,
    var onStartDeliveryClick: (orderId: Int) -> Unit
) :
    RecyclerView.Adapter<WaitingForDeliveryAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: WitingForDeliveryRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = WitingForDeliveryRvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            val deliveryData = list[position]
            binding.orderIdTv.text = "#" + deliveryData.id.toString()
            binding.locationTv.text = deliveryData.area +","+ deliveryData.address
            binding.priceTv.text = deliveryData.total_price.toString() + " JOD"
            binding.productsCountTv.text = deliveryData.item_count.toString()
        }
        holder.binding.startDeliveryButton.setOnClickListener {
            onStartDeliveryClick(list[position].id)
        }
        holder.itemView.setOnClickListener {
            onClickListener(list[position].id)
        }
    }
}