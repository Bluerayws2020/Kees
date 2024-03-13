package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityResetPassword2Binding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.isInputEmpty
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPassword2Binding

    private var phoneNumber: String? = null
    private var otpCode: String? = null
    private val viewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPassword2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.includedTap.title.text = getString(R.string.change_password)
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()

            //  Log.d("LKASSD", phoneNumber)
        }
        phoneNumber = intent.getStringExtra("phoneNumber")
        otpCode = intent.getStringExtra("otpCode")
        binding.includedTap.back.hide()
        binding.continueBtn.setOnClickListener {

            if (binding.Password.isInputEmpty()) {
                HelperUtils.showMessage(this, "Please Enter New Password")
                return@setOnClickListener
            }
            if (binding.confirmPassword.isInputEmpty()) {
                HelperUtils.showMessage(this, "Please Enter Confirm Password")
                return@setOnClickListener
            }

            val password = binding.Password.text.toString()
            val confirm = binding.confirmPassword.text.toString()

            Log.d("LKJBN", phoneNumber.toString())
            Log.d("LKJBN", otpCode.toString())
            viewModel.retrieveResetPasswordRequest(
                phoneNumber!!,
                otpCode!!,
                password,
                confirm
            )
        }
        getData()
    }

    private fun getData() {
        viewModel.getResetPasswordRequest().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(
                            this,
                            result.data.message ?: result.data.data.toString()
                        )
                        startActivity(Intent(this, LoginActivity::class.java))
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