package com.blueray.kees.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.NotificationAdapter
import com.blueray.kees.databinding.FragmentNotificationBinding
import com.blueray.kees.ui.activities.HomeActivity


class NotificationFragment : Fragment() {

    private lateinit var binding : FragmentNotificationBinding
    private lateinit var adapter : NotificationAdapter

    override fun onResume() {
        super.onResume()
        (requireContext() as HomeActivity).setSelectedId(R.id.notificationFragment)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = NotificationAdapter(listOf())
        binding.notificationRv.adapter = adapter
        binding.notificationRv.layoutManager = LinearLayoutManager(requireContext())
    }
}