package com.ht.qrscanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ht.qrscanner.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // after 3 sec it move to next screen that is home screen
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(HomeActivity.getCallingIntent(this@SplashActivity))
            finish()
        }, 3000)

    }
}