package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityMyProfileBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class MyProfileActivity : BaseActivity() {

    private val viewModel: AppViewModel by viewModels()
    private lateinit var binding : ActivityMyProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up app bar
        binding.includedTap.title.text = getString(R.string.profile)
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()

        // call Api
        viewModel.retrieveCustomerProfile()

        // observe to Live Data
        observeToLiveData()

        binding.changeNumber.setOnClickListener {
            startActivity(Intent(this,ChangePhoneNumber::class.java))
        }
        binding.personalDataBtn.setOnClickListener{
            startActivity(Intent(this,MyProfileDataActivity::class.java))
        }
        binding.addressBtn.setOnClickListener {
            startActivity(Intent(this,AddLocationActivity::class.java))
        }
        binding.walletBalence.setOnClickListener{
            startActivity(Intent(this ,WalletActivity::class.java))
        }

    }

    private fun observeToLiveData() {
        viewModel.getCustomerProfile().observe(this){
            result->
            when (result) {
                is NetworkResults.Success -> {
                    val data = result.data.data
                    if(result.data.status == 200){
                        binding.name.text = data.full_name
                        binding.walletBalence.text = data.wallet_balance
                        binding.phoneNumberTV.text = data.phone
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