package com.blueray.kees.ui.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityLoadingBinding
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.helpers.ViewUtils.startAnimation
import kotlinx.coroutines.*

class LoadingActivity : BaseActivity() {

    private lateinit var binding :ActivityLoadingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e("ayham", "loading Activity")



        val animation =
            AnimationUtils.loadAnimation(this, R.anim.laoding_back_animation).apply {
                duration = 700
                interpolator = AccelerateDecelerateInterpolator()
            }

        val productsAnimation =
            AnimationUtils.loadAnimation(this, R.anim.product_animation).apply {
            duration = 700
            interpolator = AccelerateDecelerateInterpolator()
        }
        val backgroundDisposeAnimation =
            AnimationUtils.loadAnimation(this, R.anim.background_despose_scale).apply {
                duration = 700
                interpolator = AccelerateDecelerateInterpolator()
            }
        val rotationImageAnimation =
            AnimationUtils.loadAnimation(this, R.anim.rotation_image_animation).apply {
                duration = 700
                interpolator = AccelerateDecelerateInterpolator()
            }

        lifecycleScope.launch(Dispatchers.Main) {
            delay(2000)
            async {
                binding.movingBackground.startAnimation(animation){
                    binding.movingBackground1.show()
                    binding.movingBackground.hide()
                }
            }
            async {
                binding.dinamicImage.startAnimation(productsAnimation){
                    binding.dinamicImage.hide()
                    binding.dinamicImage2.show()
                    binding.imageView.hide()
                    binding.imageView2.show()
                }
            }

            delay(100)
            async {
                binding.logo.hide()
            }
            async {
                binding.logo2.show()
            }
            async {
                val anim2 = ObjectAnimator.ofInt(binding.progress, "progress", 20, 100)
                anim2.duration = 400
                anim2.start()
            }
            delay(2000)

            async {
                binding.movingBackground1.startAnimation(backgroundDisposeAnimation){
                    binding.movingBackground1.hide()
                }
            }
            async {
                binding.imageView2.visibility = View.INVISIBLE
                binding.dinamicImage2.visibility = View.INVISIBLE
                binding.progress.visibility = View.INVISIBLE
                binding.logo2.startAnimation(rotationImageAnimation){
                    binding.logo2.hide()

                }
            }
            delay(600)
            startActivity(Intent(this@LoadingActivity, ChooseLanguageActivity::class.java))
            overridePendingTransition(0, 0)

        }



    }
}