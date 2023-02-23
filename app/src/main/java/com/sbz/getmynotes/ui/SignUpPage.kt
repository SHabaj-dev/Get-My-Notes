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
            try {
                mEmail = binding.email.text.toString()
                mPassword = binding.passwordSignUp.text.toString()
                mConfirmPassword = binding.confirmPass.text.toString()
                mUserName = binding.tvUserName.text.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (mEmail.isEmpty() || mPassword.isEmpty() || mConfirmPassword.isEmpty() || mUserName.isEmpty()) {
                Toast.makeText(this, "Please Fill the Credentials Carefully", Toast.LENGTH_SHORT)
                    .show()
            } else if (!mPassword.equals(mConfirmPassword)) {
                Toast.makeText(this, "Password didn't match.", Toast.LENGTH_SHORT).show()
            }else{
                createUser(mEmail, mPassword, mUserName)
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
                    if (user != null) {
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build()

                        user.updateProfile(profileUpdate)
                            .addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    Log.d("USER NAME SAVED", userName)
                                } else {
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