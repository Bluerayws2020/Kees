package com.blueray.kees.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityChangeLanguageBinding
import com.blueray.kees.helpers.ContextWrapper
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.ui.AppViewModel
import java.util.Locale

class ChooseLanguageActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChangeLanguageBinding
    private val viewModel: AppViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (HelperUtils.getToken(this) != "") {
            if (HelperUtils.getDriverStatus(this) != "") {
                val intent = Intent(this, DriverHomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }



        binding.arCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.enCheckBox.isChecked = false
            }
        }
        binding.enCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.arCheckBox.isChecked = false
            }
        }
//        binding.continueBtn.setOnClickListener {
//            if (intent?.getBooleanExtra("fromHome", false) ?: false) {
//                startActivity(Intent(this, HomeActivity::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                })
//                finish()
//            } else {
//                startActivity(Intent(this, OnBoardingActivity::class.java))
//            }
//        }

        binding.arLanguageBtn.setOnClickListener(this)
        binding.enLanguageBtn.setOnClickListener(this)
        binding.continueBtn.hide()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        var language = ""
        when (v?.id) {
            R.id.en_language_btn -> language = "en"

            R.id.ar_language_btn -> language = "ar"
        }
//        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
//        sharedPreferences.edit().putString("lang", language).apply()
        HelperUtils.setLang(this, language)
        val intentSplash = Intent(this, UserTypeLoginActivity::class.java)
        startActivity(intentSplash)
        finishAffinity()
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }

}