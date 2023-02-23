package com.sbz.getmynotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.sbz.getmynotes.MainActivity
import com.sbz.getmynotes.R
import com.sbz.getmynotes.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var mEmail: String
    private lateinit var mPassword: String
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_page)

        binding.tvSignUp.setOnClickListener {
            navToSignUpPage()
        }

        binding.tvForgotPassword.setOnClickListener {
            navToForgotPasswordPage()
        }


        binding.btnLogin.setOnClickListener {
            mEmail = binding.email.text.toString()
            mPassword = binding.password.toString()
            if (mEmail.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
            }
            if (mPassword.isEmpty()) {
                Toast.makeText(this, "Password can't be empty!!!", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(mEmail, mPassword)
            }
        }

    }


    private fun loginUser(e_mail: String, password: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(e_mail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginPage, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Login Failed!!\n Please Try Again.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


    private fun navToForgotPasswordPage() {
        /*Implement Code to change activity to forgot password*/
    }

    private fun navToSignUpPage() {
        val intent = Intent(this@LoginPage, SignUpPage::class.java)
        startActivity(intent)
    }
}