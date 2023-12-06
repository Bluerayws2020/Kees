package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.R
import com.blueray.kees.databinding.CartItemBinding
import com.blueray.kees.model.WeeklyBasketProduct
import com.bumptech.glide.Glide

class CartItemAdapter(
    var onPlusClickListener: (productId: Int, quantity: String, colorId: Int, sizeId: Int, unitId: Int, weight: Int, position: Int, quantityShow: Int) -> Unit,
) : RecyclerView.Adapter<CartItemAdapter.MyViewHolder>() {


    val differ = AsyncListDiffer(this,callbacks)

    private object callbacks :DiffUtil.ItemCallback<WeeklyBasketProduct>(){
        override fun areContentsTheSame(
            oldItem: WeeklyBasketProduct,
            newItem: WeeklyBasketProduct
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: WeeklyBasketProduct,
            newItem: WeeklyBasketProduct
        ): Boolean {
            return ((oldItem.quantity == newItem.quantity)||
                    (oldItem.product_id == newItem.product_id))
        }

    }

    inner class MyViewHolder(var binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = differ.currentList[position]

        holder.apply {
            Glide.with(holder.itemView.context).load(data.image).placeholder(R.drawable.tahini)
                .into(binding.productImage)
            binding.productName.text = data.product_name
            binding.price.text = data.product_price
            binding.itemCount.text = data.quantity.toString()
        }
        // handel on Item Click
        holder.binding.addItem.setOnClickListener {
            //add item
            holder.binding.itemCount.text =
                (holder.binding.itemCount.text.toString().toInt() + 1).toString()
            onPlusClickListener(
                data.product_id,
                data.quantity.toString(),
                data.color_id,
                data.size_id,
                data.unit_id,
                data.weight_id,
                position,
                holder.binding.itemCount.text.toString().toInt()
            )
        }
        holder.binding.removeItem.setOnClickListener {
            //decrement item
            if (holder.binding.itemCount.text.toString().toInt() > 0) {
                holder.binding.itemCount.text =
                    (holder.binding.itemCount.text.toString().toInt() - 1).toString()
            }
            onPlusClickListener(
                data.product_id,
                data.quantity.toString(),
                data.color_id,
                data.size_id,
                data.unit_id,
                data.weight_id,
                position,
                holder.binding.itemCount.text.toString().toInt()
            )
        }

    }

    private var addToCartClick: ((id: String) -> Unit)? = null
    fun addToCartClickListener(listener: ((id: String) -> Unit)) {
        addToCartClick = listener
    }
}