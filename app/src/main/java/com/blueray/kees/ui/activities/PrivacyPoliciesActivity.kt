package com.blueray.kees.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityTermsAndConditionsBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class PrivacyPoliciesActivity : BaseActivity() {
    private lateinit var binding: ActivityTermsAndConditionsBinding
    private val viewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up app bar
        binding.includedTap.title.text = getString(R.string.privacy_and_policy)
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.back.hide()
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        // call API
        viewModel.retrievePrivacyPolicies()

        // call Observers
        getData()


    }

    private fun getData() {
        viewModel.getPrivacyPolicies().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        binding.title11.text = result.data.data.title
                        binding.text.text = result.data.data.description
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
}