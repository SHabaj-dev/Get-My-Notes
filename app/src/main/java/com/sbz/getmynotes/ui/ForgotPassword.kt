package com.sbz.getmynotes.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.sbz.getmynotes.R
import com.sbz.getmynotes.databinding.ActivityForgotPasswordBinding

@Suppress("DEPRECATION")
class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var resetCode: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

        val window: Window = window
        val decorView: View = window.decorView
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE

        mAuth = FirebaseAuth.getInstance()
        binding.ivBackForgotPass.setOnClickListener { onBackPressed() }

        binding.btnNext.setOnClickListener {
            forgotPass()
        }
    }

    private fun forgotPass() {
        val email: String = binding.forgotEmail.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            Toast.makeText(this@ForgotPassword, "Please enter a email", Toast.LENGTH_SHORT)
                .show()
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener {

                    //there are multiple task of addOnCompleteListener

                        task ->
                    if (task.isSuccessful) {
                        //resetCode = task.result?.getString("email")
                        Toast.makeText(
                            this@ForgotPassword,
                            "An Email with Password reset link is sent.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()

                    } else {
                        Toast.makeText(
                            this@ForgotPassword,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
        }
    }
}