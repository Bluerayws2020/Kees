package com.blueray.kees.ui.activities
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log.e
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair // Import this for using Pair
import com.blueray.kees.databinding.ActivitySplashBinding
import com.blueray.kees.helpers.HelperUtils.getLang
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switchLanguage(getLang(this))

        MainScope().launch {
            delay(2000)
            e("ayham", "AnimationStart")

            // Define your shared elements and their associated names
            val sharedElement1 = Pair<View, String>(binding.movingBackground, "SharedView1")
            val sharedElement2 = Pair<View, String>(binding.logo, "SharedView2")

            // Create an array of shared elements
            val sharedElements: Array<Pair<View, String>> = arrayOf(sharedElement1, sharedElement2)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@SplashActivity,
                *sharedElements // Spread the array of shared elements
            )

            startActivity(
                Intent(this@SplashActivity, LoadingActivity::class.java),
                options.toBundle()

            )
            finish()
        }
    }

}
