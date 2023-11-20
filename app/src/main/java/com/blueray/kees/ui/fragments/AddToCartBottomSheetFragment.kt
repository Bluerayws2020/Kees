package com.blueray.kees.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DayOfTheWeekAdapter
import com.blueray.kees.databinding.FragmentAddToCartBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddToCartBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentAddToCartBottomSheetBinding
    private lateinit var adapter :DayOfTheWeekAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddToCartBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init adapter
        initAdapter()
    }

    private fun initAdapter() {
        adapter = DayOfTheWeekAdapter(listOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")){
            // todo implement click
        }
        val lm = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.dayRv.adapter = adapter
        binding.dayRv.layoutManager = lm
    }

}