package com.sbz.getmynotes.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sbz.getmynotes.R
import com.sbz.getmynotes.databinding.ActivitySignUpPageBinding

class SignUpPage : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpPageBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mEmail: String
    private var mUniversityName: String = ""
    private lateinit var mPassword: String
    private lateinit var mUserName: String
    private lateinit var mConfirmPassword: String
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_page)

        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.tvLogin.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.btnSignUp.setOnClickListener {
            binding.btnSignUp.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            try {
                mEmail = binding.email.text.toString().trim()
                mPassword = binding.passwordSignUp.text.toString().trim()
                mConfirmPassword = binding.confirmPass.text.toString().trim()
                mUserName = binding.tvUserName.text.toString().trim()
                mUniversityName = binding.uniName.text.toString().trim()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (mEmail.isEmpty() || mPassword.isEmpty() || mConfirmPassword.isEmpty() || mUserName.isEmpty()) {
                Toast.makeText(this, "Please Fill the Credentials Carefully", Toast.LENGTH_SHORT)
                    .show()
                binding.btnSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else if (!mPassword.equals(mConfirmPassword)) {
                Toast.makeText(this, "Password didn't match.", Toast.LENGTH_SHORT).show()
                binding.btnSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else if (mPassword.length < 8) {
                Toast.makeText(
                    this,
                    "Password must be at least 8 characters long",
                    Toast.LENGTH_SHORT
                ).show()
                binding.btnSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else {

                createUser(mEmail, mPassword, mUserName)
            }
        }
    }

    private fun createUser(e_mail: String, password: String, userName: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(e_mail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserInfo()
                } else {
                    Toast.makeText(
                        this@SignUpPage,
                        "Something went Wrong!! Please Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.btnSignUp.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
    }

    private fun updateUserInfo() {

        val timeStamp = System.currentTimeMillis()

        val uid = mAuth.uid

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = mEmail
        hashMap["userName"] = mUserName
        hashMap["profileImage"] = ""
        hashMap["userType"] = "user"
        hashMap["timeStamp"] = timeStamp
        hashMap["universityName"] = mUniversityName


        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Saving User Info Success", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed Saving User Info", Toast.LENGTH_SHORT).show()

            }
    }

}