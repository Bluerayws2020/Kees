package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.R
import com.blueray.kees.databinding.VariationsRvItemBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.VariationsMultiple

class ProductVariationsAdapter(
    var list: List<VariationsMultiple>,
    var onClickListener: (color_id: Int, size_id: Int, weight_id: Int, unit_id: Int) -> Unit
) : RecyclerView.Adapter<ProductVariationsAdapter.MyViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class MyViewHolder(var binding: VariationsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            VariationsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            if (data.on_sale_price_status == "Active") {
                priceTv.text = data.on_sale_price
                oldPriceTv.text = data.sale_price
            } else {
                priceTv.text = data.sale_price
                oldPriceTv.hide()
            }
            colorTv.text = data.color_name
            sizeTv.text = data.size_name + " " + data.unit_name
            weightTv.text = data.weight_name
            radioBtn.isChecked = data.selected
            // Check if the current item is selected
            if (selectedPosition == position) {

                (root as CardView).setBackgroundResource(R.drawable.card_view_background)

            } else {
                (root as CardView).setBackgroundResource(R.drawable.unselected_card_view_background)

            }
        }

        // Set click listener for each item
        holder.itemView.setOnClickListener {
            // Update selected position
            selectedPosition = holder.adapterPosition

            // Update selected status in your data model
            list.forEachIndexed { index, item ->
                item.selected = (index == selectedPosition)
            }
            notifyDataSetChanged()

            // Invoke the click listener
            onClickListener(data.color_id, data.size_id, data.weight_id, data.unit_id)
        }
    }

    override fun getItemCount(): Int = list.size
}
