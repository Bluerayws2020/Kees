package com.blueray.kees.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityDriverHomeBinding

class DriverHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDriverHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navDrawer.setNavigationItemSelectedListener{
            item ->
            when(item.itemId){
                R.id.profile->{
                    navController.navigate(R.id.driverProfileFragment)
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    true
                }
                else ->{
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    false
                }
            }
        }


    }
    fun openDrawer(){
        binding.drawerlayout.openDrawer(GravityCompat.START)
    }
}