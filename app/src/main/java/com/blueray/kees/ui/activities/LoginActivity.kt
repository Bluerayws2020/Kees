package com.blueray.kees.ui.activities;

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityLoginActivtiyBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.FromLogin
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginActivtiyBinding
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginActivtiyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this, "ar")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        hideProgress()

        //observe to live data
        getData()

        binding.includedTap.back.setOnClickListener {
            onBackPressed()
        }

        binding.createNewAccount.setOnClickListener {
            val intentHome = Intent(this, CreateAccountActivity::class.java)
            startActivity(intentHome)

        }
        binding.continueBtn.setOnClickListener {
            viewModel.retrieveLogin(
                binding.phoneNumberET.text.toString(),
                binding.password.text.toString()
            )
        }
    }


    private fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun hideProgress() {
        binding.createNewAccountBtn.show()
        binding.progressBar.hide()
    }

    private fun showProgress() {
        binding.continueBtn.hide()
        binding.progressBar.show()
    }

    private fun getData() {
        viewModel.getLogin().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        if (result.data.isAuth != null && !result.data.isAuth) {
                            HelperUtils.saveUserToken(this, result.data.token)
                            startActivity(Intent(this, OtpActivity::class.java).apply {
                                putExtra("phoneNumber",binding.phoneNumberET.text.toString())
                            })
                            FromLogin = true
                            return@observe
                        }
                        HelperUtils.saveUserData(this, result.data.customer_data)
                        HelperUtils.saveUserToken(this, result.data.token)
                        startActivity(Intent(this, HomeActivity::class.java))
//                            HelperUtils.OTP = result.data.customer_data.otp_code.toString()

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