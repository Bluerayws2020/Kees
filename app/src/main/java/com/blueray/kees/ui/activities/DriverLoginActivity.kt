package com.blueray.kees.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityDriverLoginBinding
import com.blueray.kees.helpers.HelperUtils

class DriverLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDriverLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        HelperUtils.setDefaultLanguage(this, "ar")
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.includedTap.title.text = "Driver Portal"
        binding.includedTap.back.setOnClickListener {
            onBackPressed()
        }
        binding.continueBtn.setOnClickListener {
            startActivity(Intent(this , DriverHomeActivity::class.java))
        }
    }

}