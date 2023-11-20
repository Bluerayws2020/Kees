package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.R
import com.blueray.kees.databinding.CategoryHomeRvItemBinding
import com.blueray.kees.databinding.HomeSliderItemBinding
import com.blueray.kees.model.GetMainCategoriesData
import com.bumptech.glide.Glide

class AllCategoriesAdapter (
    // todo change model
    var list: List<GetMainCategoriesData>,
    var onClickListener : (id : String)->Unit
): RecyclerView.Adapter<AllCategoriesAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: CategoryHomeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CategoryHomeRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.apply {
            binding.name.text = data.name
            Glide.with(binding.root.context).load(data.image).placeholder(R.drawable.candy).into(binding.catImage)
        }
        holder.itemView.setOnClickListener {
            onClickListener(data.id.toString())
        }
    }

}