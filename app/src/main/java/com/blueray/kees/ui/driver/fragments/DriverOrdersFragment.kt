package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.blueray.kees.R
import com.blueray.kees.adapters.OrdersViewPagerAdapter
import com.blueray.kees.databinding.FragmentDriverOrdersBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity
import com.google.android.material.tabs.TabLayoutMediator


class DriverOrdersFragment : Fragment() {
    private lateinit var binding: FragmentDriverOrdersBinding
    var tabTitle = arrayOf("Delivering", "Waiting for delivery")
    private val viewModel: AppViewModel by viewModels()

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
        viewModel.retrieveDriverProfile()
        getDriverData()
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
    private fun getDriverData() {
        viewModel.getDriverProfile().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        binding.driverNameTv.text = result.data.data.full_name
                        binding.driverNumberTv.text = result.data.data.id.toString()
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

}