package com.sbz.getmynotes.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbz.getmynotes.databinding.ActivityEditPdfBinding

class EditPdfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPdfBinding
    private var subjectId = ""
    private lateinit var subjectsTitleList: ArrayList<String>
    private lateinit var subjectIdArrayList: ArrayList<String>
    private var selectedSubjectId = ""
    private var selectedSubjectTitle = ""
    private var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subjectId = intent.getStringExtra("subjectId")!!
//        Toast.makeText(this, subjectId, Toast.LENGTH_LONG).show()
//        subjectId = intent.getStringExtra("subjectId_pdfEdit")!!

        loadCategories()


        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnUploadPdf.setOnClickListener {
            binding.pbUpdatePdf.visibility = View.VISIBLE
            validateData()


        }


        binding.etCourse.setOnClickListener {
            categoryDialogue()
        }

    }

    private fun validateData() {
        topic = binding.etTopicName.text.toString().trim()
        if (topic.isEmpty()) {
            Toast.makeText(this, "Topic Can't be Empty", Toast.LENGTH_SHORT).show()
        } else if (selectedSubjectId.isEmpty()) {
            Toast.makeText(this, "Please Select A Subject", Toast.LENGTH_SHORT).show()
        } else {
            updatePdf()
        }
    }

    private fun updatePdf() {
        val hashMap = HashMap<String, Any>()
        hashMap["topic"] = topic
        hashMap["subjectId"] = selectedSubjectId

        val ref = FirebaseDatabase.getInstance().getReference("Notes")
        ref.child(subjectId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                binding.pbUpdatePdf.visibility = View.GONE
                Toast.makeText(this, "Updated Successfully...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Uploading Failed due to ${it.message}", Toast.LENGTH_SHORT)
                    .show()
                binding.pbUpdatePdf.visibility = View.GONE
            }
    }


    private fun categoryDialogue() {
        val subjectArray = arrayOfNulls<String>(subjectsTitleList.size)
        for (i in subjectsTitleList.indices) {
            subjectArray[i] = subjectsTitleList[i]
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Subject")
            .setItems(subjectArray) { dialog, position ->
                selectedSubjectId = subjectIdArrayList[position]
                selectedSubjectTitle = subjectsTitleList[position]

                binding.etCourse.text = selectedSubjectTitle
            }
            .show()
    }

    private fun loadCategories() {
        subjectsTitleList = ArrayList()
        subjectIdArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Subjects")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                subjectsTitleList.clear()
                subjectIdArrayList.clear()

                for (ds in snapshot.children) {
                    val id = ds.child("id").value.toString()
                    val subject = ds.child("subject").value.toString()
                    subjectsTitleList.add(subject)
                    subjectIdArrayList.add(id)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}