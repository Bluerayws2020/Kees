package com.blueray.kees.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityUpdateProfileBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.isInputEmpty
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding
    private val viewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up app bar
        binding.includedTap.title.text = getString(R.string.change_password)
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()

        binding.continueBtn.setOnClickListener {

            if(binding.OldPasswordEt.isInputEmpty()){
                showMessage(this,"Please Enter Old Password")
                return@setOnClickListener
            }
            if(binding.passwordEt.isInputEmpty()){
                showMessage(this,"Please Enter New Password")
                return@setOnClickListener
            }
            if(binding.confirmPasswordEt.isInputEmpty()){
                showMessage(this,"Please Enter Confirm Password")
                return@setOnClickListener
            }
            val old = binding.OldPasswordEt.text.toString()
            val new = binding.passwordEt.text.toString()
            val confirm= binding.confirmPasswordEt.text.toString()

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
                        HelperUtils.showMessage(this, result.data.message)
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