package com.blueray.kees.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityDriverHomeBinding

class DriverHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDriverHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
    fun openDrawer(){
        binding.drawerlayout.openDrawer(GravityCompat.START)
    }
}