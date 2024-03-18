package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityContactUsBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.fragments.HomeFragment

class ContactUsActivity : BaseActivity() {
    private lateinit var binding: ActivityContactUsBinding
    private val viewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareAppBar(getString(R.string.contact_us))
        binding.sendButton.setOnClickListener {
            viewModel.retrieveContactUs(
                binding.phoneNumberET.text.toString(),
                binding.establishmentNameET.text.toString(),
                binding.notesEt.text.toString()
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
    private fun getData(){
        viewModel.getContactUs().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(this , result.data.message)
                        Log.d("LKPAZX" , result.data.toString())
                        val intent = Intent(this , HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        this,
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
        }
    }
