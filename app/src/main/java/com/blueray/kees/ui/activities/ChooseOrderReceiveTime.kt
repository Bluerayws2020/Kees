package com.blueray.kees.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityChooseOrderRecievalTimeBinding
import com.blueray.kees.helpers.HelperUtils.AREA
import com.blueray.kees.helpers.HelperUtils.DATE_OF_BIRTH
import com.blueray.kees.helpers.HelperUtils.FULL_NAME
import com.blueray.kees.helpers.HelperUtils.GENDER
import com.blueray.kees.helpers.HelperUtils.LAT
import com.blueray.kees.helpers.HelperUtils.LONG
import com.blueray.kees.ui.AppViewModel
import kotlinx.coroutines.launch

class ChooseOrderReceiveTime : BaseActivity() {
    
    private lateinit var binding : ActivityChooseOrderRecievalTimeBinding
    private val viewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseOrderRecievalTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.continueBtn.setOnClickListener {

            startActivity(Intent(this,OtpActivity::class.java))
        }
        binding.includedTap.title.text= getString(R.string.create_accounttt)
        binding.previousBtn.setOnClickListener {
            onBackPressed()
        }
        
    }
}