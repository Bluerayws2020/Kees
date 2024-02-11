package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentDriverHomeBinding
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity


class DriverHomeFragment : Fragment() {

    private lateinit var binding:FragmentDriverHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriverHomeBinding.inflate(layoutInflater)

        binding.includedTap.menuButton.setOnClickListener {
            (activity as DriverHomeActivity).openDrawer()
        }
        binding.startWorkingBtn.setOnClickListener {
            findNavController().navigate(R.id.driverOrdersFragment)
        }
        return binding.root
    }


}