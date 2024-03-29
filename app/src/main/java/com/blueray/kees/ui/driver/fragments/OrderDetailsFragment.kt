package com.blueray.kees.ui.driver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentDriversLastOrderBinding
import com.blueray.kees.databinding.FragmentOrderDetails2Binding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity


class OrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetails2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetails2Binding.inflate(layoutInflater)
        prepareAppBar(getString(R.string.orderdetails))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewProductsButton.setOnClickListener {
            findNavController().navigate(R.id.orderProductsFragment)
        }
        binding.completeOrderButton.setOnClickListener {
            val dialog = DoneDialogFragment()
            dialog.show(childFragmentManager,"showDialog")
        }
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