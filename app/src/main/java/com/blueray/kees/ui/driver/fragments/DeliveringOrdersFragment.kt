package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DeliveringOrdersAdapter
import com.blueray.kees.databinding.FragmentDeliveringOrdersBinding


class DeliveringOrdersFragment : Fragment() {

    private lateinit var binding: FragmentDeliveringOrdersBinding
    private lateinit var adapter: DeliveringOrdersAdapter

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
        initAdapter()

    }

    private fun initAdapter() {

        adapter = DeliveringOrdersAdapter(listOf()){
            findNavController().navigate(R.id.orderDetailsFragment)
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.deliveringOrdersRv.adapter = adapter
        binding.deliveringOrdersRv.layoutManager = layoutManager
    }


}