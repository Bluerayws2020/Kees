package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.adapters.WaitingForDeliveryAdapter
import com.blueray.kees.databinding.FragmentWaitingForDeliveryOrdersBinding


class WaitingForDeliveryOrdersFragment : Fragment() {

    private lateinit var binding : FragmentWaitingForDeliveryOrdersBinding
    private lateinit var adapter : WaitingForDeliveryAdapter

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
        initAdapter()
    }

    private fun initAdapter() {
        adapter = WaitingForDeliveryAdapter(listOf()){

        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.waitingForDeliveryOrdersRv.adapter = adapter
        binding.waitingForDeliveryOrdersRv.layoutManager = layoutManager
    }

}