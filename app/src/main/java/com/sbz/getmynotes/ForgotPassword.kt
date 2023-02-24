package com.sbz.getmynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sbz.getmynotes.databinding.ActivityForgotPasswordBinding

class ForgotPassword : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
       // setContentView(R.layout.activity_forgot_password)


    }
}