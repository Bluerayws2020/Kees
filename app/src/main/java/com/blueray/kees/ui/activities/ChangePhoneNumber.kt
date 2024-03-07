package com.blueray.kees.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityChangePhoneNumberBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.FromLogin
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.isInputEmpty
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class ChangePhoneNumber : BaseActivity() {
    private lateinit var binding : ActivityChangePhoneNumberBinding
    private val viewModel : AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up app bar
        prepareAppBar(getString(R.string.changePhoneNumber))

        // observe to live Data
        changeMyPhoneObserver()

        binding.continueBtn.setOnClickListener{
            if(binding.phoneNumberET.isInputEmpty()){
                showMessage(this,"Please Enter Phone Number")
                return@setOnClickListener
            }

            val dialog = Dialog(this)

            // Set a transparent background for the dialog window
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.yes_no_dialog)

            val yesBtn = dialog.findViewById<Button>(R.id.yesBtn)
            val noBtn = dialog.findViewById<Button>(R.id.noBtn)

            yesBtn.setOnClickListener {
                viewModel.retrieveChangePhoneNumber(binding.phoneNumberET.text.toString())
            }
            noBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()

        }

    }
    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

    private fun changeMyPhoneObserver() {
        viewModel.getChangePhoneNumber().observe(this){
                result->
            when (result) {
                is NetworkResults.Success -> {
                    if(result.data.status == 200){
                        FromLogin = true// set this var to true to go back to the home screen after OTP
                        startActivity(Intent(this,OtpActivity::class.java))
                        finish()
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