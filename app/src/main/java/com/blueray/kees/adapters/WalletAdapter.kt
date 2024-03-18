package com.blueray.kees.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.kees.R
import com.blueray.kees.databinding.TransactionItemBinding
import com.blueray.kees.model.WalletData

class WalletAdapter(
    var list: List<WalletData>
) : RecyclerView.Adapter<WalletAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: TransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            title.text = data.wallet_transaction_type
            if (data.wallet_balance_after > data.wallet_balance_before) {
                val difference =
                    data.wallet_balance_after.toFloat() - data.wallet_balance_before.toFloat()
                transactionAmount.text = " + " + difference.toString()
                transactionAmount.setTextColor(R.color.add_color)
            } else {
                val difference =
                    data.wallet_balance_before.toFloat() - data.wallet_balance_after.toFloat()
                transactionAmount.text = " - " + difference.toString()
                transactionAmount.setTextColor(R.color.minus_color)
            }

        }
    }
}