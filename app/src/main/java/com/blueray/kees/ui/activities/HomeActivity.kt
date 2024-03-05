package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityHomeBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.helpers.ViewUtils.startAnimation

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navDrawer, navController!!)
        binding.bottomNavigationView.setItemSelected(R.id.homeFragment, true)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it) {
                R.id.homeFragment -> {
                    navController!!.navigate(R.id.homeFragment)
                }

                R.id.searchFragment -> {
                    navController!!.navigate(R.id.searchFragment)

                }

                R.id.favorite -> {
                    navController!!.navigate(R.id.favoriteFragment)
                }

                R.id.notificationFragment -> {
                    navController!!.navigate(R.id.notificationFragment)
                }
            }
        }


//        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController!!)
        binding.navDrawer.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController!!.navigate(R.id.homeFragment)
                    binding.bottomNavigationView.setItemSelected(R.id.homeFragment, true)
                    closeDrawer()
                    true
                }

                R.id.search -> {
                    navController!!.navigate(R.id.searchFragment)
                    binding.bottomNavigationView.setItemSelected(R.id.searchFragment, true)
                    closeDrawer()
                    true
                }

                R.id.favorite -> {
                    navController!!.navigate(R.id.favoriteFragment)
                    binding.bottomNavigationView.setItemSelected(R.id.favorite, true)
                    closeDrawer()
                    true
                }

                R.id.notifications -> {
                    navController!!.navigate(R.id.notificationFragment)
                    binding.bottomNavigationView.setItemSelected(R.id.notificationFragment, true)
                    closeDrawer()
                    true
                }

                R.id.profile -> {
                    startActivity(Intent(this, MyProfileActivity::class.java).apply {
                        // todo add extras
                    })
                    closeDrawer()
                    true
                }

                R.id.aboutUs -> {
                    startActivity(Intent(this, AboutUsActivity::class.java).apply {
                        // todo add extras
                    })
                    closeDrawer()
                    true
                }

                R.id.privacyPolicy -> {
                    startActivity(Intent(this, PrivacyPoliciesActivity::class.java).apply {
                        // todo add extras
                    })
                    closeDrawer()
                    true
                }

                R.id.customerService -> {
                    startActivity(Intent(this@HomeActivity, ContactUsActivity::class.java))
                    closeDrawer()
                    true
                }

                R.id.changeLanguage -> {
                    startActivity(
                        Intent(
                            this@HomeActivity,
                            ChooseLanguageActivity::class.java
                        ).apply {
                            putExtra("fromHome", true)
                        })
                    closeDrawer()
                    true
                }

                R.id.exit -> {
                    val intent = Intent(this, UserTypeLoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    HelperUtils.logOutUser(this)
                    startActivity(intent)
                    finish()
                    true
                }

                else -> {
                    false
                }
            }
        }


    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun animateHideBottomNav() {
        binding.bottomNavCard.show()
        binding.bottomNavigationView.show()
        val animation = AnimationUtils.loadAnimation(this, R.anim.botom_nav_hide_animation).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()

        }
        binding.bottomNavCard.startAnimation(animation) {
            binding.bottomNavCard.hide()
            binding.bottomNavigationView.hide()
            isBottomShowing = false
        }
    }

    fun animateShowBottomNav() {
        binding.bottomNavCard.show()
        binding.bottomNavigationView.show()
        val animation = AnimationUtils.loadAnimation(this, R.anim.botom_show_animation).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()

        }
        binding.bottomNavCard.startAnimation(animation) {
            isBottomShowing = true

        }
    }

    fun setSelectedId(id: Int) {
        binding.bottomNavigationView.setItemSelected(id, true)
    }

    private var isBottomShowing = true
    fun getIsBottomShowing(): Boolean {
        return this.isBottomShowing
    }
}