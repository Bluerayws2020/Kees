package com.blueray.kees.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.FinishedOrdersAdapter
import com.blueray.kees.adapters.PastOrdersAdapter
import com.blueray.kees.databinding.ActivityCustomerPastOrdersBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class CustomerPastOrdersActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCustomerPastOrdersBinding
    private val viewModel: AppViewModel by viewModels()
    private lateinit var adapter: PastOrdersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerPastOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareAppBar("Past Orders")
        viewModel.retrieveCustomerPastOrders()
        getPastOrdersData()
    }
    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }
    private fun getPastOrdersData(){
        viewModel.getCustomerPastOrders().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
//                        initAdapter()
                        adapter = PastOrdersAdapter(result.data.data , {})
                        adapter.onItemClick ={orderId ->
                            val bundle = bundleOf("orderId" to orderId.toString(), "fromFinished" to "1")
                           // findNavController().navigate(R.id.orderDetailsFragment , bundle)
                        }
                        binding.pastOrdersRv.adapter = adapter
                        binding.pastOrdersRv.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//                        Log.d("MNBVC" , adapter.list.size.toString())
                    }else {
                        HelperUtils.showMessage(this, getString(R.string.Error))

                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(this, result.data?.message ?: getString(R.string.Error))

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }
}