package com.sbz.getmynotes

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sbz.getmynotes.databinding.ActivityMainBinding
import com.sbz.getmynotes.ui.MainFragment
import com.sbz.getmynotes.ui.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedTab = 1
    private val mainFragment = MainFragment()
    private val settingsFragment = SettingsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        window.statusBarColor = this.resources.getColor(R.color.home_20)

        setFirstFragment()
        binding.tvSettings.visibility = View.GONE
        binding.hiMsg.visibility=View.GONE
        binding.ivUserProfile.visibility=View.GONE
        binding.tvUserName.visibility=View.GONE
        binding.tvAllCourse.visibility=View.VISIBLE


        //save the original params
        /*val originalParams1 = binding.ivUserProfile.layoutParams
        val originalParams2 = binding.hiMsg.layoutParams
        val originalParams3=binding.tvUserName.layoutParams


        //
        val params1 = binding.ivUserProfile.layoutParams as LinearLayout.LayoutParams
        val params2 = binding.hiMsg.layoutParams as LinearLayout.LayoutParams
        val params3=binding.tvUserName.layoutParams as LinearLayout.LayoutParams

        params1.weight = 0.0f
        //params1.width = 0
        params2.weight = 0.0f
       // params2.width = 0
        params3.weight = 0.0f
       // params3.width = 0

        binding.ivUserProfile.layoutParams = params1
        binding.hiMsg.layoutParams = params2
        binding.tvUserName.layoutParams = params3
        binding.hiMsg.visibility=View.GONE
        binding.ivUserProfile.visibility=View.GONE
        binding.tvUserName.visibility=View.GONE


        binding.tvAllCourse.visibility=View.VISIBLE*/

        binding.homeLayout.setOnClickListener {
            if (selectedTab != 1) {

                getUserName()
                setFirstFragment()

                binding.tvSettings.visibility = View.GONE
                binding.hiMsg.visibility=View.GONE
                binding.ivUserProfile.visibility=View.GONE
                binding.tvUserName.visibility=View.GONE
                binding.tvAllCourse.visibility=View.VISIBLE
                /*val params4 = binding.ivUserProfile.layoutParams as LinearLayout.LayoutParams
                val params5 = binding.hiMsg.layoutParams as LinearLayout.LayoutParams
                val params6=binding.tvUserName.layoutParams as LinearLayout.LayoutParams

                params4.weight = 0.0f
               // params1.width = 0
                params5.weight = 0.0f
               // params2.width = 0
                params6.weight = 0.0f
                //params3.width = 0

                binding.ivUserProfile.layoutParams = params1
                binding.hiMsg.layoutParams = params2
                binding.tvUserName.layoutParams = params3

                binding.hiMsg.visibility=View.GONE
                binding.ivUserProfile.visibility=View.GONE
                binding.tvUserName.visibility=View.GONE
                binding.tvAllCourse.visibility=View.VISIBLE*/




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

                setSecondFragment()

//                binding.tvUserName.text = "Profile"
//                binding.hiMsg.visibility = View.GONE
//                binding.ivUserProfile.setImageResource(R.drawable.arrow_back)
                binding.tvSettings.visibility = View.VISIBLE
                binding.hiMsg.visibility=View.VISIBLE
                binding.ivUserProfile.visibility=View.VISIBLE
                binding.tvUserName.visibility=View.VISIBLE
                binding.tvAllCourse.visibility=View.GONE

                //setting the original params
             /*   binding.ivUserProfile.layoutParams = originalParams1
                binding.hiMsg.layoutParams = originalParams2
                binding.tvUserName.layoutParams = originalParams3*/




                binding.tvHome.visibility = View.GONE

                binding.ivHome.setImageResource(R.drawable.home_icon)
                binding.homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                binding.tvSettings.visibility = View.VISIBLE
                binding.ivSettings.setImageResource(R.drawable.settings_icon)
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

    private fun setSecondFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, settingsFragment)
            commit()
        }
        binding.ivUserProfile.setOnClickListener {
            setFirstFragment()
        }
    }

    private fun setFirstFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, mainFragment)
            commit()
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

//            Log.d("SBZ_USER_NAME", mUserName)
//            Log.d("SBZ_EMAIL", email)
//            Log.d("SBZ_PHOTO_URL", photoUrl)
//            Log.d("SBZ_EMAIL_VERIFIED", emailVerified)
//            Log.d("SBZ_UID", uid)
        }
        binding.tvUserName.text = mUserName
        binding.hiMsg.visibility = View.VISIBLE
        binding.ivUserProfile.setImageResource(R.drawable.person_icon)
    }

}