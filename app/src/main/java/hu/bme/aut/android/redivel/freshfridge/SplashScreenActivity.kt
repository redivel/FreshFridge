package hu.bme.aut.android.redivel.freshfridge

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.redivel.freshfridge.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animation = AnimationUtils.loadAnimation(baseContext, R.anim.zoom)

        binding.SplashImg.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.root.visibility = View.GONE
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
    }
}