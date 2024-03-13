package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityResetPasswordBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResetPasswordOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private var phoneNumber: String? = null
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        phoneNumber = intent.getStringExtra("phoneNumber")
        setGif()
        getData()
        binding.continueBtn.setOnClickListener {
            if (binding.otpView.otp.isNullOrEmpty()) {
                HelperUtils.showMessage(this, "Please Enter Otp")
                return@setOnClickListener
            }
            if (binding.otpView.otp.toString().length != 6) {
                HelperUtils.showMessage(this, "Wrong Otp")
                return@setOnClickListener
            }
            if (phoneNumber != null) {
                val intent = Intent(this, ResetPasswordActivity::class.java)
                intent.putExtra("phoneNumber", phoneNumber)
                intent.putExtra("otpCode", binding.otpView.otp.toString())
                startActivity(intent)

            }
        }
        val delayTime = 3000L * 60
        var job: Job? = null
        binding.resend.hide()
        job = lifecycleScope.launch {
            async {
                delay(delayTime)
                binding.resend.show()
                this@launch.cancel()
            }
        }

        binding.resend.setOnClickListener {
            binding.resend.hide()
            job.start()
        }
    }

    private fun setGif() {
        binding.imageView.loadGIFResource(this, R.drawable.phone_gif)
    }

    private fun getData() {
        viewModel.getSendOtp().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(
                            this,
                            result.data.message ?: result.data.data.toString()
                        )
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