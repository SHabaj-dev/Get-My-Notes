package com.sbz.getmynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sbz.getmynotes.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_page)

        binding.tvSignUp.setOnClickListener {
            navToSignUpPage()
        }

        binding.tvForgotPassword.setOnClickListener {
            navToForgotPasswordPage()
        }


    }




    private fun navToForgotPasswordPage(){
        /*Implement Code to change activity to forgot password*/
    }

    private fun navToSignUpPage(){
        val intent = Intent(this@LoginPage, SignUpPage::class.java)
        startActivity(intent)
    }
}