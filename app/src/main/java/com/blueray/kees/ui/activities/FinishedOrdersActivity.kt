package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.FinishedOrdersAdapter
import com.blueray.kees.databinding.ActivityFinishedOrdersBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.driver.fragments.OrderDetailsFragment

class FinishedOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishedOrdersBinding
    private lateinit var adapter: FinishedOrdersAdapter
    private val viewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishedOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareAppBar("Finished Orders")
        viewModel.retrieveFinishedOrders()
        getFinishedOrdersData()
    }

    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

    fun initAdapter() {
        binding.finishedOrdersRv.adapter = adapter
        binding.finishedOrdersRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
    private fun getFinishedOrdersData(){
        viewModel.getFinishedOrders().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
//                        initAdapter()
                        adapter = FinishedOrdersAdapter(result.data.data ,{})
                        adapter.onItemClick ={

                        }
                        binding.finishedOrdersRv.adapter = adapter
                        binding.finishedOrdersRv.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        Log.d("MNBVC" , adapter.list.size.toString())
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