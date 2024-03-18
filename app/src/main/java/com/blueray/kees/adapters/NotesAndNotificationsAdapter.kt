package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.NotesListItemBinding
import com.blueray.kees.model.NotificationsData

class NotesAndNotificationsAdapter(
    val list: List<NotificationsData>,
) :
    RecyclerView.Adapter<NotesAndNotificationsAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: NotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            NotesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (list.size > 3){
            return 3
        }else{
            return list.size
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.apply {
            binding.noteTextTv.text = data.notification_text
        }
    }
}