package com.blueray.kees.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.NotificationAdapter
import com.blueray.kees.api.DrawerOpener
import com.blueray.kees.databinding.FragmentNotificationBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.HomeActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var adapter: NotificationAdapter
    private val viewModel: AppViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        //(requireContext() as HomeActivity).setSelectedId(R.id.notificationFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        binding.includedTap.back.hide()
        binding.includedTap.title.text = getString(R.string.notifications)
        binding.includedTap.menu.setOnClickListener {
            (activity as? DrawerOpener)?.openDrawer()
        }
        binding.deleteAll.setOnClickListener {
            viewModel.retrieveDeleteAllNotifications()
        }
        viewModel.retrieveNotifications()
        getNotificationsData()
        getDeleteNotificationData()
        getDeleteAllNotificationData()
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = NotificationAdapter(listOf(), {}, {message, date -> })
        binding.notificationRv.adapter = adapter
        binding.notificationRv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getNotificationsData() {
        viewModel.getNotifications().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter = NotificationAdapter(result.data.data, {}, {
                            message, date ->
                        })
                        adapter.onNotificationClick = { message , date ->
                            val dialogFragment = NotificationPopUp.newInstance(message, date)
                            dialogFragment.show(childFragmentManager, "YourDialogFragmentTag")
                        }
                        adapter.onDeleteClick = {
                            viewModel.retrieveDeleteNotificationById(it)
                        }
                        binding.notificationRv.adapter = adapter
                        binding.notificationRv.layoutManager = LinearLayoutManager(requireContext())
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))

                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        requireContext(),
                        result.data?.message ?: getString(R.string.Error)
                    )

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }

    private fun getDeleteNotificationData() {
        viewModel.getDeleteNotificationById().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter.notifyDataSetChanged()
                        GlobalScope.launch {
                            delay(200)
                            viewModel.retrieveNotifications()
                        }
                        HelperUtils.showMessage(requireContext(), result.data.data)
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))

                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        requireContext(),
                        result.data?.message ?: getString(R.string.Error)
                    )

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }

    private fun getDeleteAllNotificationData() {
        viewModel.getDeleteAllNotifications().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter.notifyDataSetChanged()
                        GlobalScope.launch {
                            delay(200)
                            viewModel.retrieveNotifications()
                        }
                        HelperUtils.showMessage(requireContext(), result.data.data)
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))

                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        requireContext(),
                        result.data?.message ?: getString(R.string.Error)
                    )

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }

}