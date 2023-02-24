package com.sbz.getmynotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD:app/src/main/java/com/sbz/getmynotes/ForgotPassword.kt
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.sbz.getmynotes.databinding.ActivityForgotPasswordBinding
import com.sbz.getmynotes.ui.SignUpPage

class ForgotPassword : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var resetCode: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
       // setContentView(R.layout.activity_forgot_password)
        mAuth = FirebaseAuth.getInstance()
        binding.nextBtn.setOnClickListener {


            val email:String = binding.forgotEmail.text.toString().trim{ it <= ' '}
            if (email.isEmpty())
            {
                Toast.makeText(this@ForgotPassword,"Please enter a email",Toast.LENGTH_SHORT)
                    .show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{

                        //there are multiple task of addOnCompleteListener

                        task->
                        if (task.isSuccessful){
                            //resetCode = task.result?.getString("email")
                            Toast.makeText(this@ForgotPassword,"Email sent successfully to reset your password",Toast.LENGTH_SHORT)
                                .show()
                            //finish()
                            //start otp activity
                            val intent = Intent(this@ForgotPassword, VerifyOtp::class.java)
                            startActivity(intent)

                        }else{
                            Toast.makeText(this@ForgotPassword,task.exception!!.message.toString(),Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

            }

        }


=======
import com.sbz.getmynotes.R

class VerifyOtp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)
>>>>>>> main:app/src/main/java/com/sbz/getmynotes/ui/VerifyOtp.kt
    }
}