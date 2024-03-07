package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.LocationsAdapter
import com.blueray.kees.databinding.ActivityAddLoctionBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class AddLocationActivity : BaseActivity() {
    private lateinit var binding: ActivityAddLoctionBinding
    private lateinit var adapter: LocationsAdapter
    private val viewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLoctionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up app bar
        prepareAppBar(getString(R.string.MyLocations))
        // init Adapter
        initAdapter()

        // observe to live Data
        getAddresses()
        deleteAddressObserver()


        binding.addLocationBtn.setOnClickListener {
            startActivity(Intent(this, AddNewLocationActivity::class.java))
        }

    }
    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }
    private fun getAddresses() {
        viewModel.getCustomerAddresses().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter.list = result.data.data
                        adapter.notifyDataSetChanged()
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(this, result.data?.message ?: getString(R.string.Error))
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }
    private fun deleteAddressObserver() {
        viewModel.getCustomerDeleteAddress().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        viewModel.retrieveCustomerAddresses()
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(this, result.data?.message ?: getString(R.string.Error))
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }


    private fun initAdapter() {
        adapter = LocationsAdapter(listOf(), {
            // On edit
            addressId , title ->
            startActivity(Intent(this, ChangeLocationActivity::class.java).apply {
                putExtra("address_id",addressId)
                putExtra("title",title)
            })
        }, {
            // on delete
            viewModel.retrieveCustomerDeleteAddress(it)
        })
        val lm = LinearLayoutManager(this)
        binding.addressesRv.adapter = adapter
        binding.addressesRv.layoutManager = lm
    }

    override fun onResume() {
        super.onResume()
        viewModel.retrieveCustomerAddresses()
    }
}