package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentDriverProfileBinding
import com.blueray.kees.databinding.FragmentOrderProductsBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity

class DriverProfileFragment : Fragment() {
    private lateinit var binding: FragmentDriverProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDriverProfileBinding.inflate(layoutInflater)

        // Prepare AppBar
        prepareAppBar()

        return binding.root
    }

    private fun prepareAppBar() {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            (activity as DriverHomeActivity).onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
    }
}