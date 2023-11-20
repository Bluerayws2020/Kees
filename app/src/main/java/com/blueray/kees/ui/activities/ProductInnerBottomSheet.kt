package com.blueray.kees.ui.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DayOfTheWeekAdapter
import com.blueray.kees.databinding.FragmentProductInnerBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ProductInnerBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentProductInnerBottomSheetBinding
    private lateinit var adapter: DayOfTheWeekAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProductInnerBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init adapter of days
        initAdapter()
    }

    private fun initAdapter() {
        adapter = DayOfTheWeekAdapter(listOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")){
            // todo implement click
        }
        val lm = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.daysRv.adapter = adapter
        binding.daysRv.layoutManager = lm
    }

}