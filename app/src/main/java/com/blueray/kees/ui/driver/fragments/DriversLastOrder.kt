package com.blueray.kees.ui.driver.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DeliveringOrdersAdapter
import com.blueray.kees.adapters.DriverLastOrdersAdapter
import com.blueray.kees.databinding.FragmentDriversLastOrderBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity

class DriversLastOrder : Fragment() {
    private lateinit var binding : FragmentDriversLastOrderBinding
    private lateinit var adapter : DriverLastOrdersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDriversLastOrderBinding.inflate(layoutInflater)

        // prepare App Bar
        prepareAppBar(getString(R.string.past_orders))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init Adapter
        initAdapter()

    }

    private fun initAdapter() {

        adapter = DriverLastOrdersAdapter(listOf()){
            pastePhoneNumberInPhoneApp("0785756979",requireContext())
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.lastOrdersRv.adapter = adapter
        binding.lastOrdersRv.layoutManager = layoutManager
    }
    private fun pastePhoneNumberInPhoneApp(phoneNumber: String, context: Context) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
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