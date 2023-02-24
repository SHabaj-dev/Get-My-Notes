package com.sbz.getmynotes

import android.animation.AnimatorInflater
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.databinding.DataBindingUtil
import com.sbz.getmynotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedTab = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.homeLayout.setOnClickListener {
            if(selectedTab != 1){

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
            if(selectedTab != 2){

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

}