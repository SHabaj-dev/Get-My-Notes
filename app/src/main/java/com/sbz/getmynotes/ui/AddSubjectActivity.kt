package com.sbz.getmynotes.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sbz.getmynotes.databinding.ActivityAddSubjectBinding

class AddSubjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSubjectBinding
    private lateinit var mAuth: FirebaseAuth
    private var category = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        category = binding.etCourse.text.toString().trim()

        if (category.isEmpty()) {
            Toast.makeText(this, "Enter Subject..", Toast.LENGTH_SHORT).show()
        } else {
            addSubjectToFireBase()
        }
    }

    private fun addSubjectToFireBase() {
        binding.pbSubmit.visibility = View.VISIBLE

        val timeStamp = System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timeStamp"
        hashMap["subject"] = category
        hashMap["timestamp"] = timeStamp
        hashMap["uid"] = "${mAuth.uid}"


//        ->  Database Root -> Subjects -> subjectId -> subject info
        val ref = FirebaseDatabase.getInstance().getReference("Subjects")
        ref.child("$timeStamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                binding.pbSubmit.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                binding.pbSubmit.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Something Went Wrong ${e.printStackTrace()}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
}