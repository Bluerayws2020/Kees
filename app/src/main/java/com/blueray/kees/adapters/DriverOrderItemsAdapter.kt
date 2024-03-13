package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.OrderDetailsRvItemBinding
import com.blueray.kees.model.DriverWeeklyBasketProduct
import com.blueray.kees.model.SaleOperation

class DriverOrderItemsAdapter(
    var list: List<DriverWeeklyBasketProduct> = listOf(),
    var finishedList: List<SaleOperation> = listOf(),
    val onClickListener: (id: Int) -> Unit
) :
    RecyclerView.Adapter<DriverOrderItemsAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: OrderDetailsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            OrderDetailsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (list.isNotEmpty()) {
            return list.size
        } else {
            return finishedList.size
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            if (list.isNotEmpty()) {
                val data = list[position]
                binding.numberOfItemsTv.text = data.quantity.toString()
                binding.productNameTv.text = data.product.name_en
                binding.priceTv.text = data.product.on_sale_price
            } else {
                val data = finishedList[position]
                binding.numberOfItemsTv.text = data.quantity.toString()
                binding.productNameTv.text = data.product.name_en
                binding.priceTv.text = data.end_total
                binding.productCategoryTv.text = data.product.category_name

            }

        }
    }
}