package com.sbz.getmynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View

class SplashScreen : AppCompatActivity() {
    private val TIME_OUT = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        hideActionAndNavBar()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreen, LoginPage::class.java)
            startActivity(intent)
            finish()
        }, TIME_OUT.toLong())
    }

    @Suppress("DEPRECATION")
    private fun hideActionAndNavBar() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}