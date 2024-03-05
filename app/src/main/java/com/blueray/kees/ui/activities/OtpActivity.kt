package com.blueray.kees.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityOtpBinding
import com.blueray.kees.helpers.HelperUtils.FromLogin
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import kotlinx.coroutines.*

class OtpActivity : BaseActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val viewModel: AppViewModel by viewModels()
    private var phoneNumber: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        phoneNumber = intent.getStringExtra("phoneNumber")
        setGif()

        getData()

        binding.phoneNumberTV.text = intent.getStringExtra("phoneNumber")

        binding.continueBtn.setOnClickListener {
            if (binding.otpView.otp.isNullOrEmpty()) {
                showMessage(this, "Please Enter Otp")
                return@setOnClickListener
            }
            if (binding.otpView.otp.toString().length != 6) {
                showMessage(this, "Wrong Otp")
                return@setOnClickListener
            }
            if (phoneNumber != null) {
                viewModel.retrieveSendOtp(
                    otp_code = binding.otpView.otp.toString(),
                    phone = phoneNumber!!
                )
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

    @SuppressLint("ResourceType")
    private fun setGif() {
        binding.imageView.loadGIFResource(this, R.drawable.phone_gif)
    }

    private fun getData() {
        viewModel.getSendOtp().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        if (FromLogin) {
                            startActivity(Intent(this, HomeActivity::class.java))
                            FromLogin = !FromLogin
                            // todo add token and userData since this Api is not returning it
                        } else {
                            startActivity(Intent(this, ChooseOrderReceiveTime::class.java))
                        }
                    } else {
                        showMessage(this, getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    showMessage(this, result.data?.message ?: getString(R.string.Error))
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    showMessage(this, getString(R.string.Error))
                }
            }
        }
    }

}