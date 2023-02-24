package com.sbz.getmynotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.sbz.getmynotes.MainActivity
import com.sbz.getmynotes.R

class SplashScreen : AppCompatActivity() {
    private val TIME_OUT = 2000
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        hideActionAndNavBar()
        mAuth = FirebaseAuth.getInstance()
        Handler(Looper.getMainLooper()).postDelayed({
            if(mAuth.currentUser != null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginPage::class.java))
                finish()
            }
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