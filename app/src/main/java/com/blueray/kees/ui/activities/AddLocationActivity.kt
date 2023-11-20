package com.blueray.kees.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.kees.R
import com.blueray.kees.helpers.HelperUtils

class AddLocationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_loction)
        switchLanguage(HelperUtils.getLang(this))
    }
}