package com.blueray.kees.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.kees.R
import com.blueray.kees.helpers.HelperUtils

class AddNewLocationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_location)
        switchLanguage(HelperUtils.getLang(this))
    }
}