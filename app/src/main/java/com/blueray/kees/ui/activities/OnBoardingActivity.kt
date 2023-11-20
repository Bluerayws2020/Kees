package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import com.blueray.kees.adapters.OnBoardingAdapter
import com.blueray.kees.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : BaseActivity() {

    private lateinit var binding : ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set viewpager with indicator
        binding.viewPager.adapter = OnBoardingAdapter(supportFragmentManager,lifecycle)
        binding.indicator.setViewPager(binding.viewPager)

        // add fake swipe
        binding.next.setOnClickListener {
            if(binding.viewPager.currentItem == 2){
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem+1, true)
            }else{
//                start LogIn Activity
                startActivity(Intent(this,LoginActivity::class.java))
            }
        }

    }
}