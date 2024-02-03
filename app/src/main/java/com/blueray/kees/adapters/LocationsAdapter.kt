package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.AddressItemBinding
import com.blueray.kees.model.CustomerGetAddressesData

class LocationsAdapter(
    var list: List<CustomerGetAddressesData>,
    var onClickListener: (addressId: String, title: String) -> Unit,
    var onDeleteListener: (addressId: String) -> Unit
) : RecyclerView.Adapter<LocationsAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: AddressItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.apply {
            binding.title.text = data.title
            binding.addressArea.text = "${data.address} - ${data.area}"
            binding.details.text = data.city_name
        }
        holder.itemView.setOnClickListener {
            onClickListener.invoke(data.id.toString(), data.title)
        }

        holder.binding.deleteLocationBtn.setOnClickListener {
            onDeleteListener.invoke(data.id.toString())
        }
    }

}