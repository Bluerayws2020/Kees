package com.blueray.kees.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityDriverLoginBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.saveDriverStatus
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class DriverLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDriverLoginBinding
    private val viewModel:AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        HelperUtils.setDefaultLanguage(this, "ar")
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.includedTap.title.text = getString(R.string.drivers_portal)
        binding.includedTap.back.setOnClickListener {
            onBackPressed()
        }
        binding.continueBtn.setOnClickListener {
            validateFields()
        }
        getData()
    }

    private fun validateFields() {
        if (binding.emailEt.text.isEmpty()) {
            binding.emailEt.setError("Required Field")
        } else if (binding.passwordEt.text.isEmpty()) {
            binding.passwordEt.setError("Required Field")
        } else {
            viewModel.retrieveDriverLogin(
                binding.emailEt.text.toString(),
                binding.passwordEt.text.toString()
            )
        }
    }


    private fun getData() {
        viewModel.getDriverLogin().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.saveDriverData(this, result.data.data)
                        HelperUtils.saveUserToken(this, result.data.data.token)
                        saveDriverStatus(this , result.data.data.driver_status)
                        startActivity(Intent(this ,  DriverHomeActivity::class.java))
                        finish()
                    }else{
                        HelperUtils.showMessage(this , getString(R.string.Error))
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