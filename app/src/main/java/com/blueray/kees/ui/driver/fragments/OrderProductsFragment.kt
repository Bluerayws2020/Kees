package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DriverLastOrdersAdapter
import com.blueray.kees.adapters.DriverOrderItemsAdapter
import com.blueray.kees.adapters.WaitingForDeliveryAdapter
import com.blueray.kees.databinding.FragmentOrderProductsBinding
import com.blueray.kees.databinding.OrderDetailsRvItemBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity


class OrderProductsFragment : Fragment() {
    private lateinit var binding: FragmentOrderProductsBinding
    private lateinit var adapter: DriverOrderItemsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderProductsBinding.inflate(layoutInflater)

        // Prepare App bar
        prepareAppBar(getString(R.string.products))
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init Adapter
        initAdapter()

    }

    private fun initAdapter() {
        adapter = DriverOrderItemsAdapter(listOf()){
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.productsDetailsRv.adapter = adapter
        binding.productsDetailsRv.layoutManager = layoutManager
    }

    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            (activity as DriverHomeActivity).onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

}