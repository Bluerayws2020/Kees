package com.blueray.kees.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityDriverHomeBinding
import com.blueray.kees.helpers.HelperUtils

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
                R.id.homeFragment ->{
                    navController.navigate(R.id.driverHomeFragment)
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.ordersFragment ->{
                    navController.navigate(R.id.driverOrdersFragment)
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.previousOrdersFragment ->{
                    val intent = Intent(this ,  FinishedOrdersActivity::class.java)
                    startActivity(intent)
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    false
                }
                R.id.notifications ->{
                   // navController.navigate(R.id.notificationFragment2)
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.exit ->{
                    val intent = Intent(this, UserTypeLoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    HelperUtils.logOutUser(this)
                    startActivity(intent)
                    finish()
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