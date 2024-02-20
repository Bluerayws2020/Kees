package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blueray.kees.R
import com.blueray.kees.adapters.OrdersViewPagerAdapter
import com.blueray.kees.databinding.FragmentDriverOrdersBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity
import com.google.android.material.tabs.TabLayoutMediator


class DriverOrdersFragment : Fragment() {
    private lateinit var binding: FragmentDriverOrdersBinding
    var tabTitle = arrayOf("Delivering", "Waiting for delivery")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriverOrdersBinding.inflate(layoutInflater)

        // Prepare App Bar
        prepareAppBar(getString(R.string.orders))

        binding.viewPager.adapter = OrdersViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
        return binding.root
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