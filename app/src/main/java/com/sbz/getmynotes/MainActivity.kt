package com.sbz.getmynotes

import android.animation.AnimatorInflater
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sbz.getmynotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedTab = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        window.statusBarColor = this.resources.getColor(R.color.home_20)


        getUserName()

        binding.homeLayout.setOnClickListener {
            if (selectedTab != 1) {

                binding.tvSettings.visibility = View.GONE
                binding.ivSettings.setImageResource(R.drawable.settings_icon)
                binding.settingsLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                binding.tvHome.visibility = View.VISIBLE
                binding.ivHome.setImageResource(R.drawable.home_icon_selected)
                binding.homeLayout.setBackgroundResource(R.drawable.round_back_home_100)

                val scaleAnimation = ScaleAnimation(
                    0.8f,
                    1.0f,
                    1f,
                    1f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f
                )
                scaleAnimation.duration = 200
                scaleAnimation.isFillEnabled = true
                binding.homeLayout.startAnimation(scaleAnimation)

                selectedTab = 1
            }
        }

        binding.settingsLayout.setOnClickListener {
            if (selectedTab != 2) {

                binding.tvHome.visibility = View.GONE
                binding.ivHome.setImageResource(R.drawable.home_icon)
                binding.homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                binding.tvSettings.visibility = View.VISIBLE
                binding.ivSettings.setImageResource(R.drawable.settings_icon_slected)
                binding.settingsLayout.setBackgroundResource(R.drawable.round_back_settings_100)

                val scaleAnimation = ScaleAnimation(
                    0.8f,
                    1.0f,
                    1f,
                    1f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f
                )
                scaleAnimation.duration = 200
                scaleAnimation.isFillEnabled = true
                binding.settingsLayout.startAnimation(scaleAnimation)

                selectedTab = 2
            }
        }

    }


    private fun getUserName() {
        var mUserName = ""
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            mUserName = it.displayName.toString()

            val email = it.email.toString()
            val photoUrl = it.photoUrl.toString()

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified.toString()

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid.toString()

            Log.d("SBZ_USER_NAME", mUserName)
            Log.d("SBZ_EMAIL", email)
            Log.d("SBZ_PHOTO_URL", photoUrl)
            Log.d("SBZ_EMAIL_VERIFIED", emailVerified)
            Log.d("SBZ_UID", uid)
        }
        binding.tvUserName.text = mUserName
    }

}