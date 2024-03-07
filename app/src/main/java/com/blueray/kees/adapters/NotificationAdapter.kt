package com.blueray.kees.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.databinding.NotifictaionItemBinding
import com.blueray.kees.model.NotificationsData

class NotificationAdapter(
// todo change model
    var list: List<NotificationsData>,
    var onDeleteClick: (id: String) -> Unit,
    var onNotificationClick: (message: String, date: String) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: NotifictaionItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            NotifictaionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            bodyTv.text = data.notification_text
            dateTv.text = data.date
        }
        holder.itemView.setOnClickListener {
            onNotificationClick(data.notification_text , data.date)
        }
        holder.binding.imageView6.setOnClickListener {
            onDeleteClick(data.id.toString())
        }
    }
}
