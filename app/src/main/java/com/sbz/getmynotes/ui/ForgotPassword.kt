package com.sbz.getmynotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sbz.getmynotes.R
import com.sbz.getmynotes.databinding.ActivityForgotPasswordBinding

@Suppress("DEPRECATION")
class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

        binding.ivBackForgotPass.setOnClickListener { onBackPressed() }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, VerifyOtp::class.java))
            finish()
        }
    }
}