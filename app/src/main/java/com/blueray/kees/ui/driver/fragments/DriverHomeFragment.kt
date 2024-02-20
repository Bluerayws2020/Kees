package com.blueray.kees.ui.driver.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DriverLastOrdersAdapter
import com.blueray.kees.adapters.NotesAndNotificationsAdapter
import com.blueray.kees.adapters.NotificationAdapter
import com.blueray.kees.databinding.FragmentDriverHomeBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity


class DriverHomeFragment : Fragment() {

    private lateinit var binding:FragmentDriverHomeBinding
    private lateinit var notesAdapter : NotesAndNotificationsAdapter
    private lateinit var notificationsAdapter : NotesAndNotificationsAdapter
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

        binding.startWorkingBtn.setOnClickListener {
            findNavController().navigate(R.id.driverOrdersFragment)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Adapters
        initNoteAdapter()
        initNotificationsAdapter()
    }

    private fun initNotificationsAdapter() {
        notificationsAdapter = NotesAndNotificationsAdapter(listOf()){
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.NotificationsRv.adapter = notificationsAdapter
        binding.NotificationsRv.layoutManager = layoutManager
    }

    private fun initNoteAdapter() {
        notesAdapter = NotesAndNotificationsAdapter(listOf()){
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.notesRv.adapter = notesAdapter
        binding.notesRv.layoutManager = layoutManager
    }


}