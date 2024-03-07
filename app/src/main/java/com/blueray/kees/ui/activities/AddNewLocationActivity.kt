package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityAddNewLocationBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.isInputEmpty
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class AddNewLocationActivity : BaseActivity() {

    private lateinit var binding: ActivityAddNewLocationBinding
    private val viewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includedTap.title.text = getString(R.string.change_location)

        binding.includedTap.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.mapIcon.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.includedTap.title.text = getString(R.string.create_accounttt)

        binding.continueBtn.setOnClickListener {
            validateInput()
        }

        // observe to live Data
        getData()

    }

    private fun validateInput() {
        if (HelperUtils.LAT.isEmpty()) {
            HelperUtils.showMessage(this, getString(R.string.chooseLocation))
            return
        }
        if (binding.areaEt.isInputEmpty()) {
            HelperUtils.showMessage(this, getString(R.string.enter_area))
            return
        }

        HelperUtils.CITY = "5" // todo add Cities
        HelperUtils.AREA = binding.areaEt.text.toString()
        viewModel.retrieveCustomerAddNewAddress(
            binding.titleEt.text.toString(),

            HelperUtils.LAT,
            HelperUtils.LONG,
            HelperUtils.CITY,
            HelperUtils.AREA,
            binding.addressInDetailEt.text.toString()
        )
    }


    private fun getData() {
        viewModel.getCustomerAddNewAddress().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(this, result.data.message ?: "Address Updated")
                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        this,
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }
}