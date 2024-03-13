package com.blueray.kees.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityChangePasswordBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.isInputEmpty
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChangePasswordBinding
    private val viewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // set up app bar
        binding.includedTap.title.text = getString(R.string.change_password)
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        observeToLiveData()
        binding.continueBtn.setOnClickListener {

            if(binding.oldPassword.isInputEmpty()){
                showMessage(this,"Please Enter Old Password")
                return@setOnClickListener
            }
            if(binding.newPassword.isInputEmpty()){
                showMessage(this,"Please Enter New Password")
                return@setOnClickListener
            }
            if(binding.confirmPassword.isInputEmpty()){
                showMessage(this,"Please Enter Confirm Password")
                return@setOnClickListener
            }
            val old = binding.oldPassword.text.toString()
            val new = binding.newPassword.text.toString()
            val confirm= binding.confirmPassword.text.toString()

            viewModel.retrieveChangePassword(
                old, new, confirm
            )
        }
    }
        private fun observeToLiveData() {
        viewModel.getChangePassword().observe(this){
                result->
            when (result) {
                is NetworkResults.Success -> {
                    if(result.data.status == 200){
                        HelperUtils.showMessage(this, result.data.message ?: result.data.data.toString() )
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