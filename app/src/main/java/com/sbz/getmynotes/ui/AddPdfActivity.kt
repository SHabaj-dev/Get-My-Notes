package com.sbz.getmynotes.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sbz.getmynotes.R
import com.sbz.getmynotes.databinding.ActivityAddPdfBinding
import com.sbz.getmynotes.model.AdminSubjectModel

class AddPdfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPdfBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var subjectArrayList: ArrayList<AdminSubjectModel>
    private var pdfUri: Uri? = null
    private val TAG = "PDF_ADD_TAG"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_pdf)

        mAuth = FirebaseAuth.getInstance()
        loadPdfCategories()

        binding.etCourse.setOnClickListener {
            categoryPickDialog()
        }

        binding.btnAttachPdf.setOnClickListener {
            pdfPickIntent()
        }

        binding.btnUploadPdf.setOnClickListener {
            binding.btnUploadPdf.visibility = View.GONE
            binding.pbUpload.visibility = View.VISIBLE
            validateData()
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private var topic = ""
    private var subject = ""
    private fun validateData() {

        topic = binding.etTopicName.text.toString().trim()
        subject = binding.etCourse.text.toString().trim()

        if (topic.isEmpty()) {
            binding.btnUploadPdf.visibility = View.VISIBLE
            binding.pbUpload.visibility = View.GONE
            Toast.makeText(this, "Topic Can't Be Empty", Toast.LENGTH_SHORT).show()
        } else if (subject.isEmpty()) {
            binding.btnUploadPdf.visibility = View.VISIBLE
            binding.pbUpload.visibility = View.GONE
            Toast.makeText(this, "Please Pick a Subject", Toast.LENGTH_SHORT).show()
        } else if (pdfUri == null) {
            binding.btnUploadPdf.visibility = View.VISIBLE
            binding.pbUpload.visibility = View.GONE
            Toast.makeText(
                this,
                "No File is Selected \n  Please select a File.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            uploadPdfToFireStore()
        }

    }

    private fun uploadPdfToFireStore() {

        val timestamp = System.currentTimeMillis()
        val filePathAndName = "Notes/$timestamp"

        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedPdfUrl = "${uriTask.result}"

                uploadPdfInfoToDb(uploadedPdfUrl, timestamp)



                Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                binding.btnUploadPdf.visibility = View.VISIBLE
                binding.pbUpload.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Upload Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
                binding.btnUploadPdf.visibility = View.VISIBLE
                binding.pbUpload.visibility = View.GONE

            }
    }

    private fun uploadPdfInfoToDb(uploadedPdfUrl: String, timestamp: Long) {

        val uid = mAuth.uid

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"] = "$timestamp"
        hashMap["topic"] = topic
        hashMap["subjectId"] = selectedSubjectId
        hashMap["url"] = uploadedPdfUrl
        hashMap["timestamp"] = timestamp
        hashMap["viewCount"] = 0
        hashMap["downloadsCount"] = 0

        val ref = FirebaseDatabase.getInstance().getReference("Notes")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Updated DB Successfully", Toast.LENGTH_SHORT).show()
                pdfUri = null
            }
            .addOnFailureListener {
                Toast.makeText(this, "Updating DB failed", Toast.LENGTH_SHORT).show()
            }

    }


    private fun loadPdfCategories() {
        subjectArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Subjects")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                subjectArrayList.clear()
                for (ds in snapshot.children) {
                    val subject = ds.getValue(AdminSubjectModel::class.java)
                    subjectArrayList.add(subject!!)
                    Log.d(TAG, "onDataChange: ${subject.subject}")

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private var selectedSubjectId = ""
    private var selectedSubjectTitle = ""
    private fun categoryPickDialog() {
        Log.d(TAG, "categoryPickDialog: Showing pdf Category")

        val categoriesArray = arrayOfNulls<String>(subjectArrayList.size)
        for (i in subjectArrayList.indices) {
            categoriesArray[i] = subjectArrayList[i].subject
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Subject")
            .setItems(categoriesArray) { dialog, which ->
                selectedSubjectId = subjectArrayList[which].id
                selectedSubjectTitle = subjectArrayList[which].subject


                Log.d(
                    TAG,
                    "categoryPickDialog: Id -> $selectedSubjectId Subject -> $selectedSubjectTitle"
                )
                binding.etCourse.text = selectedSubjectTitle
            }
            .show()
    }

    private fun pdfPickIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val pickPdfIntent = Intent.createChooser(intent, "Select PDF")
        resultLauncher.launch(pickPdfIntent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                pdfUri = result.data!!.data
                Log.d(TAG, "pdfPickIntent: PDF uri -> $pdfUri")
            } else {
                Toast.makeText(this, "Pdf Selection Failed.\n Please Try Again", Toast.LENGTH_SHORT)
                    .show()
            }
        }
}