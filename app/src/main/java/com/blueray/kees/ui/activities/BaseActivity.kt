package com.blueray.kees.ui.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.kees.helpers.ContextWrapper
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.getLang
import com.blueray.kees.helpers.HelperUtils.setLang
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {


    // to save the context of the resources and save the language if destroyed
    val deviceLocale = Locale.getDefault()
    val languageCode = deviceLocale.language
    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        // set flags to full screen if you want to hid status bar
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
//        switchLanguage(getLang(this))
    }
    fun switchLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = resources
        val configuration = Configuration()
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }



}