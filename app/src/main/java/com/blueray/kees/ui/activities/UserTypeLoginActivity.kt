package com.blueray.kees.ui.activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityUserTypeLoginBinding
import com.blueray.kees.helpers.HelperUtils

class UserTypeLoginActivity : BaseActivity() {
    private lateinit var binding: ActivityUserTypeLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserTypeLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this, "ar")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // hide the back button because its the first activity
        binding.includedTap.back.visibility = View.GONE

        binding.createNewAccountBtn.setOnClickListener {
            val intent = Intent(this , CreateAccountActivity::class.java)
            startActivity(intent)
        }
        binding.gotAnAccountBtn.setOnClickListener {
            val intent = Intent(this ,  LoginActivity::class.java)
            startActivity(intent)
        }
        //todo check how the user can enter as a guest
        binding.enterAsGuestBtn.setOnClickListener {
            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
        }
        binding.driversPortalBtn.setOnClickListener {
            val intent = Intent(this ,  DriverLoginActivity::class.java)
            startActivity(intent)
        }

    }
}