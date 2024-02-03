package com.blueray.kees.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityAboutUsBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class AboutUsActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutUsBinding
    private val viewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up app bar
        binding.includedTap.title.text = getString(R.string.about_us)
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.back.hide()
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // call API
        viewModel.retrieveAboutUs()
        getData()

    }

    private fun getData() {
        viewModel.getAboutUs().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        binding.messageText.text = result.data.data.mission
                        binding.valuesText.text = result.data.data.value
                        binding.visionText.text = result.data.data.vision

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