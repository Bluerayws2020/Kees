package com.blueray.kees.ui.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityContactUsBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class ContactUsActivity : BaseActivity() {
    private lateinit var binding: ActivityContactUsBinding
    private val viewModel: AppViewModel by viewModels()
    var phoneNumber = ""
    var whatsAppNumber = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareAppBar(getString(R.string.contact_us))
        viewModel.retrieveContactUsInfo()
        binding.sendButton.setOnClickListener {
            viewModel.retrieveContactUs(
                binding.phoneNumberET.text.toString(),
                binding.establishmentNameET.text.toString(),
                binding.notesEt.text.toString()
            )
        }
        binding.whatsAppBtn.setOnClickListener {
            openWhatsAppPage(this)
        }
        binding.phoneNumberCard.setOnClickListener {


            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        getData()
        getContactUsData()
    }

    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

    private fun getData() {
        viewModel.getContactUs().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(this, result.data.data.toString())
                        Log.d("LKPAZX", result.data.toString())
                        val intent = Intent(this, HomeActivity::class.java)
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

    private fun getContactUsData() {
        viewModel.getContactUsInfo().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                         phoneNumber = result.data.data.phone
                        binding.numberTv.text = result.data.data.phone
                        whatsAppNumber = result.data.data.whatsapp_phone
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
    private fun openWhatsAppPage(context: Context) {
        val packageName = "com.whatsapp" // Package name of the WhatsApp
        val youTubePageUrl = "https://wa.me/$whatsAppNumber" // URL of the WhatsApp

        val youTubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$whatsAppNumber"))

        val chromePackageName = "com.android.chrome" // Package name of Chrome browser
        val chromeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youTubePageUrl))
        chromeIntent.setPackage(chromePackageName)

        val defaultBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youTubePageUrl))

        try {
            // Check if the YouTube app is installed
            context.packageManager.getPackageInfo(packageName, 0)

            // YouTube app is present, open the YouTube page in it
            context.startActivity(youTubeIntent)
        } catch (e: PackageManager.NameNotFoundException) {
            try {
                // Check if Chrome browser is installed
                context.packageManager.getPackageInfo(chromePackageName, 0)

                // Chrome is installed, open the YouTube page in Chrome
                context.startActivity(chromeIntent)
            } catch (e: PackageManager.NameNotFoundException) {
                // Chrome is not installed, open the YouTube page in the default browser
                context.startActivity(defaultBrowserIntent)
            }
        }
    }
}
