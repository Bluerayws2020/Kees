package com.blueray.kees.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.blueray.kees.databinding.ActivityUserTypeLoginBinding
import com.blueray.kees.helpers.ContextWrapper
import com.blueray.kees.helpers.HelperUtils
import java.util.Locale

class UserTypeLoginActivity : BaseActivity() {
    private lateinit var binding: ActivityUserTypeLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserTypeLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        HelperUtils.setDefaultLanguage(this, "ar")
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Log.d("TOKENKKK", HelperUtils.getToken(this).toString())
        // hide the back button because its the first activity
        binding.includedTap.back.visibility = View.GONE


        Log.d("POAZX", HelperUtils.getLang(this).toString())
        binding.createNewAccountBtn.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
        binding.gotAnAccountBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        //todo check how the user can enter as a guest
        binding.enterAsGuestBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        binding.driversPortalBtn.setOnClickListener {
            val intent = Intent(this, DriverLoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}