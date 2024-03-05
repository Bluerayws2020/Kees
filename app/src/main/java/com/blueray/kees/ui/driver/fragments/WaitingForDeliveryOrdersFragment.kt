package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.WaitingForDeliveryAdapter
import com.blueray.kees.databinding.FragmentWaitingForDeliveryOrdersBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class WaitingForDeliveryOrdersFragment : Fragment() {

    private lateinit var binding : FragmentWaitingForDeliveryOrdersBinding
    private lateinit var adapter : WaitingForDeliveryAdapter
    private val viewModel: AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWaitingForDeliveryOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Adapter
        viewModel.retrieveDriverProfile()
        getDriverData()
        viewModel.retrieveDriverOrders()
        getDriverOrders()
        getDeliveryStatusData()
    }

    private fun initAdapter() {
        adapter = WaitingForDeliveryAdapter(listOf(), {},{})
        adapter.onClickListener = {
            val bundle = bundleOf("orderId" to it.toString() , "fromWaiting" to "1")
            findNavController().navigate(R.id.orderDetailsFragment, bundle)
        }
        adapter.onStartDeliveryClick ={
            GlobalScope.launch {
                viewModel.retrieveUpdateDeliveryStatus(it.toString())
                delay(300L)

                viewModel.retrieveDriverOrders()
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.waitingForDeliveryOrdersRv.adapter = adapter
        binding.waitingForDeliveryOrdersRv.layoutManager = layoutManager
    }
    private fun getDriverData() {
        viewModel.getDriverProfile().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        binding.driverNameTv.text = result.data.data.full_name
                        binding.driverNumberTv.text = result.data.data.city_name
                    }else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))

                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(requireContext(), result.data?.message ?: getString(R.string.Error))

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }
    private fun getDriverOrders(){
        viewModel.getDriverOrders().observe(this){
                result ->
            when(result){
                is NetworkResults.Success ->{
                    initAdapter()
                    adapter.list = result.data.data.waiting_for_delivery
                    adapter.notifyDataSetChanged()
                    Log.d("LKJAZZX" , result.data.data.waiting_for_delivery.toString())
                }
                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(requireContext(), result.data?.message ?: getString(R.string.Error))
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }
    private fun getDeliveryStatusData(){
        viewModel.getUpdateDeliveryStatus().observe(this){
                result ->
            when(result){
                is NetworkResults.Success ->{
                    Toast.makeText(requireContext() , result.data.message , Toast.LENGTH_LONG).show()
//                    adapter.notifyDataSetChanged()

                }
                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(requireContext(), result.data?.message ?: getString(R.string.Error))

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }
}