package com.blueray.kees.ui.activities

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun animateHideBottomNav(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.botom_nav_hide_animation).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
        }
        binding.bottomNavCard.startAnimation(animation)
    }

}