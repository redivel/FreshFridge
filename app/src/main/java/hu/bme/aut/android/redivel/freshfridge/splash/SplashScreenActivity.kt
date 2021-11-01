package hu.bme.aut.android.redivel.freshfridge.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.redivel.freshfridge.LoginActivity
import hu.bme.aut.android.redivel.freshfridge.R
import hu.bme.aut.android.redivel.freshfridge.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val animation = AnimationUtils.loadAnimation(baseContext, R.anim.zoom)

        binding.SplashImg.startAnimation(animation)

        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)

        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.SplashImg.visibility = View.GONE
                startActivity(intent)
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
    }
}