package com.sbz.getmynotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.sbz.getmynotes.R
import com.sbz.getmynotes.databinding.ActivitySignUpPageBinding

@Suppress("DEPRECATION")
class SignUpPage : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpPageBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mEmail: String
    private lateinit var mPassword: String
    private lateinit var mUserName: String
    private lateinit var mConfirmPassword: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_page)

        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.tvLogin.setOnClickListener { onBackPressed() }

        binding.btnSignUp.setOnClickListener {
            mEmail = binding.email.text.toString()
            mPassword = binding.passwordSignUp.text.toString()
            mConfirmPassword = binding.confirmPass.text.toString()
            mUserName = binding.tvUserName.text.toString()

            if (mEmail.isEmpty()) {
                Toast.makeText(this@SignUpPage, "Email can't be empty!!", Toast.LENGTH_SHORT).show()
            }
            if (mPassword.isEmpty()) {
                Toast.makeText(this@SignUpPage, "Password can't be empty!!", Toast.LENGTH_SHORT)
                    .show()
            }
            if (mConfirmPassword.isEmpty()) {
                Toast.makeText(
                    this@SignUpPage,
                    "Confirm Password can't be empty!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (mUserName.isEmpty()) {
                Toast.makeText(this@SignUpPage, "User Name can't be empty!!", Toast.LENGTH_SHORT)
                    .show()
            }

            if (mPassword.equals(mConfirmPassword)) {
                createUser(mEmail, mPassword, mUserName)
            } else {
                Toast.makeText(
                    this@SignUpPage,
                    "Password did not Match Please Check",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    private fun createUser(e_mail: String, password: String, userName: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(e_mail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@SignUpPage, "Registration Successful.", Toast.LENGTH_SHORT)
                        .show()

                    val user = mAuth.currentUser
                    if(user != null){
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build()

                        user.updateProfile(profileUpdate)
                            .addOnCompleteListener { profileTask ->
                                if(profileTask.isSuccessful){
                                    Log.d("USER NAME SAVED", userName)
                                }else{
                                    Log.d("USER NAME NOT SAVED", userName)
                                }
                            }
                    }

                    onBackPressed()
                } else {
                    Toast.makeText(
                        this@SignUpPage,
                        "Something went Wrong!! Please Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}