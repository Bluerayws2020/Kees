package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.LoctaionListItemBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.CustomerGetAddressesData

class LocationsCheckoutAdapter ( var list: List<CustomerGetAddressesData>,
                                var onClickListener: (position : Int,Data : CustomerGetAddressesData) -> Unit,
) : RecyclerView.Adapter<LocationsCheckoutAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding:LoctaionListItemBinding ) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            LoctaionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.apply {
            if(data.selected){
                binding.selectedOutline.hide()
                binding.selectedCheck.hide()
            }else{
                binding.selectedOutline.show()
                binding.selectedCheck.show()
            }
        }
        holder.itemView.setOnClickListener {
            onClickListener.invoke(position,data)
        }
    }
}