package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DeliveringOrdersAdapter
import com.blueray.kees.databinding.FragmentDeliveringOrdersBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel


class DeliveringOrdersFragment : Fragment() {

    private lateinit var binding: FragmentDeliveringOrdersBinding
    private lateinit var adapter: DeliveringOrdersAdapter
    private val viewModel: AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDeliveringOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Adapter
        viewModel.retrieveDriverProfile()

        getDriverData()
        viewModel.retrieveDriverOrders()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDriverOrders()

    }

    private fun initAdapter() {

        adapter = DeliveringOrdersAdapter(listOf()){

        }
        adapter.onClickListener = {
            val bundle = bundleOf("orderId" to it.toString() , "fromWaiting" to "0")
            findNavController().navigate(R.id.orderDetailsFragment, bundle)
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.deliveringOrdersRv.adapter = adapter
        binding.deliveringOrdersRv.layoutManager = layoutManager
    }
    private fun getDriverData() {
        viewModel.getDriverProfile().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        binding.driverNameTv.text = result.data.data.full_name
                        binding.driverLocationTv.text = result.data.data.city_name.toString()
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
                    adapter.list = result.data.data.under_delivery
                    adapter.notifyDataSetChanged()
                    Log.d("LKgggghhh" , result.data.data.under_delivery.toString())
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

    override fun onResume() {
        super.onResume()
        viewModel.retrieveDriverOrders()
//        getDriverOrders()
    }
}