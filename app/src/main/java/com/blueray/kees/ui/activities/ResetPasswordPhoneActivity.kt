package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityResetPasswordPhoneBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class ResetPasswordPhoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordPhoneBinding
    private val viewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareAppBar(getString(R.string.reset_password))
        binding.continueBtn.setOnClickListener {
            viewModel.retrieveResetPassword(
                binding.phoneNumberET.text.toString()
            )
        }
        getData()
    }

    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

    private fun getData() {
        viewModel.getResetPassword().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(this, result.data.data.toString())
                        val intent = Intent(this, ResetPasswordOtpActivity::class.java)
                        intent.putExtra("phoneNumber", binding.phoneNumberET.text.toString())
                        startActivity(intent)
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