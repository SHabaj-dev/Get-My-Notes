package com.sbz.getmynotes.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.sbz.getmynotes.R
import com.sbz.getmynotes.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var mEmail: String
    private lateinit var mPassword: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_page)
        mAuth = Firebase.auth


        binding.tvSignUp.setOnClickListener {
            navToSignUpPage()
        }

        binding.tvForgotPassword.setOnClickListener {
            navToForgotPasswordPage()
        }


        binding.btnLogin.setOnClickListener {

//            validateData()
            binding.pbLogin.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE

            try {
                mEmail = binding.email.text.toString()
                mPassword = binding.password.text.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (mEmail.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
                binding.pbLogin.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
            }
            if (mPassword.isEmpty()) {
                Toast.makeText(this, "Password can't be empty!!!", Toast.LENGTH_SHORT).show()
                binding.pbLogin.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
            } else {
                loginUser(mEmail, mPassword)
//                finish()
            }
        }


    }


    private fun checkUserType() {
        val firebaseUser = mAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userType = snapshot.child("userType").value

                    if (userType == "admin") {

                        startActivity(Intent(this@LoginPage, AdminDashboard::class.java))
                        finish()

                    } else if (userType == "user") {
                        startActivity(Intent(this@LoginPage, MainActivity::class.java))
                        finish()
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }


    private fun loginUser(e_mail: String, password: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(e_mail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    val intent = Intent(this@LoginPage, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
                    checkUserType()
                } else {
                    Toast.makeText(this, "Authentication Failed!!\n Please Try Again.", Toast.LENGTH_SHORT)
                        .show()
                    binding.pbLogin.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                }
            }
    }


    private fun navToForgotPasswordPage() {
        startActivity(Intent(this, ForgotPassword::class.java))
    }

    private fun navToSignUpPage() {
        startActivity(Intent(this, SignUpPage::class.java))
    }
}