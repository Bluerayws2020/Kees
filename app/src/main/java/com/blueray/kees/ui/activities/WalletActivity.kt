package com.blueray.kees.ui.activities

import android.os.Bundle
import com.blueray.kees.databinding.ActivityWalletBinding

class WalletActivity : BaseActivity() {
    private lateinit var binding : ActivityWalletBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set Up app bar
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }



    }
}