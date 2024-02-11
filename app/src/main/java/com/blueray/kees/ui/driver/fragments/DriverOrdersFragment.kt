package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blueray.kees.R
import com.blueray.kees.adapters.OrdersViewPagerAdapter
import com.blueray.kees.databinding.FragmentDriverOrdersBinding
import com.google.android.material.tabs.TabLayoutMediator


class DriverOrdersFragment : Fragment() {
    private lateinit var binding: FragmentDriverOrdersBinding
    var tabTitle = arrayOf("Delivering" , "Waiting for delivery")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriverOrdersBinding.inflate(layoutInflater)

        var pager = binding.viewPager
        var tab = binding.tabLayout
        pager.adapter = OrdersViewPagerAdapter(childFragmentManager ,lifecycle)
        TabLayoutMediator(tab , pager){
                tab , position ->
            tab.text = tabTitle[position]
        }.attach()
        return binding.root
    }


}