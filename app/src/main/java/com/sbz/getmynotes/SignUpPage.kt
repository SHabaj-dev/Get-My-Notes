package com.sbz.getmynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sbz.getmynotes.databinding.ActivitySignUpPageBinding

@Suppress("DEPRECATION")
class SignUpPage : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_page)

        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.tvLogin.setOnClickListener { onBackPressed() }

    }

}