package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DriverOrderItemsAdapter
import com.blueray.kees.databinding.FragmentOrderProductsBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.ui.activities.DriverHomeActivity


class OrderProductsFragment : Fragment() {
    private lateinit var binding: FragmentOrderProductsBinding
    private lateinit var adapter: DriverOrderItemsAdapter
    val productList = OrderDetailsFragment.productsList
    val finishedProductList = OrderDetailsFragment.finishedProductsList
    val note = OrderDetailsFragment.note
    val productCount = OrderDetailsFragment.productsCount
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        binding = FragmentOrderProductsBinding.inflate(layoutInflater)
        binding.notesTv.text = note
        binding.numberOfProductsTv.text = productCount.toString() + " Product"
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
        adapter = DriverOrderItemsAdapter(listOf()) {
        }
        if (productList != null) {
            adapter.list = productList
        } else{
            if (finishedProductList != null) {
                adapter.finishedList = finishedProductList
            }
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