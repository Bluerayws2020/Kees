package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityMyProfileActivtiyBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class MyProfileDataActivity : BaseActivity() {
    private lateinit var binding: ActivityMyProfileActivtiyBinding
    private val viewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileActivtiyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includedTap.title.text = getString(R.string.profile)
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        // Observe to live data
        getData()

        // Call API
        viewModel.retrieveMyProfile()

        binding.changePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        binding.changePhone.setOnClickListener {
            startActivity(Intent(this, ChangePhoneNumber::class.java))
        }
    }

    private fun getData() {
        viewModel.getMyProfile().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status == 200) {
                        val data = result.data.data
                        binding.clientName.text = data.full_name
                        binding.gender.text = data.gender
                        binding.phoneEt.text = data.phone
                        binding.birthDate.text = data.date_of_birth
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