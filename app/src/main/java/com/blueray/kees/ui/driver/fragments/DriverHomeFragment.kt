package com.blueray.kees.ui.driver.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DriverLastOrdersAdapter
import com.blueray.kees.adapters.NotesAndNotificationsAdapter
import com.blueray.kees.adapters.NotificationAdapter
import com.blueray.kees.databinding.FragmentDriverHomeBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity


class DriverHomeFragment : Fragment() {


    private lateinit var binding:FragmentDriverHomeBinding
    private lateinit var notificationsAdapter : NotesAndNotificationsAdapter
    private val viewModel:AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriverHomeBinding.inflate(layoutInflater)

        // setup App Bar
        binding.includedTap.cartLabel.hide()
        binding.includedTap.menuButton.setOnClickListener {
            (activity as DriverHomeActivity).openDrawer()
        }
        viewModel.retrieveDriverProfile()
        viewModel.retrieveNotifications()
        getDriverData()
        getNotificationsData()
        binding.startWorkingBtn.setOnClickListener {
            findNavController().navigate(R.id.driverOrdersFragment)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Adapters

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
    private fun getNotificationsData() {
        viewModel.getNotifications().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        notificationsAdapter = NotesAndNotificationsAdapter(result.data.data)
                        val layoutManager = LinearLayoutManager(requireContext())

                        binding.NotificationsRv.adapter = notificationsAdapter
                        binding.NotificationsRv.layoutManager = layoutManager
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