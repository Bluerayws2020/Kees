package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.R
import com.blueray.kees.api.ProductsAdapterListener
import com.blueray.kees.databinding.ProductsItemBinding
import com.blueray.kees.model.GetProductsData
import com.bumptech.glide.Glide

class ProductsAdapter(
    var list: List<GetProductsData>,
    var onClickListener: (id :String) -> Unit,
    var onLikeClickListener: (id : String) -> Unit,
    private val listener: ProductsAdapterListener?,
    var isWishList:Boolean
):RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ProductsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ProductsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]

        holder.apply {
            Glide.with(holder.itemView.context).load(data.image).placeholder(R.drawable.tahini).into(binding.productImage)
            binding.productName.text = data.name
            if (data.on_sale_price_status == "Active"){
                binding.price.text = data.on_sale_price
            }else{
                binding.price.text = data.sale_price
            }

            if (data.is_wishlist == true || isWishList == true){
                binding.likeBtn.isChecked = true
            }

            // must know the deference between the sale and th on sale price
        }

        // item click listener to show details
        holder.itemView.setOnClickListener {
            onClickListener(data.id.toString())
        }
        holder.apply {
        // button click listener to add to cart
            binding.addToCart.setOnClickListener {
                onClickListener(data.id.toString())
            }
            binding.likeBtn.setOnClickListener{
                onLikeClickListener(data.id.toString())
            }
        }

    }
    fun updateList(newList: List<GetProductsData>) {
        list = newList
        notifyDataSetChanged()
        if (newList.isEmpty()) {
            listener?.onListEmpty()
        }
    }

    private var addToCartClick : ((id :String) -> Unit)? = null
    fun addToCartClickListener(listener : ((id :String) -> Unit)){
        addToCartClick = listener
    }
}

