package com.sbz.getmynotes.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbz.getmynotes.R

class SplashScreen : AppCompatActivity() {
    private val TIME_OUT = 2000
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        hideActionAndNavBar()
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()
        Handler(Looper.getMainLooper()).postDelayed({
            checkUser()
        }, TIME_OUT.toLong())
    }

    private fun checkUser(){
        val firebaseUser = mAuth.currentUser
        if (firebaseUser != null) {
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userType = snapshot.child("userType").value
                        if (userType == "user") {
                            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                        } else if (userType == "admin") {
                            startActivity(Intent(this@SplashScreen, AdminDashboard::class.java))
                        }
                        finish()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle onCancelled here
                    }
                })
        } else {
            startActivity(Intent(this@SplashScreen, LoginPage::class.java))
            finish()
        }
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