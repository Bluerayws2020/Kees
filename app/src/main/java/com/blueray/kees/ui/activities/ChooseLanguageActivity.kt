package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityChangeLanguageBinding
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class ChooseLanguageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChangeLanguageBinding
    private val viewModel : AppViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueBtn.setOnClickListener {
            startActivity(Intent(this,OnBoardingActivity::class.java))
        }

    }

}