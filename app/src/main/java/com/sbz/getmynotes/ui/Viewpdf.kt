package com.sbz.getmynotes.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbz.getmynotes.R
import com.sbz.getmynotes.adapter.PdfAdminAdapter
import com.sbz.getmynotes.application.MyApplication
import com.sbz.getmynotes.databinding.ActivityViewpdfBinding

class Viewpdf : AppCompatActivity() {

    private lateinit var binding: ActivityViewpdfBinding

    var pdfTopic = ""
    var pdfId = ""
    var pdfUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewpdf)

        pdfId = intent.getStringExtra("pdfId")!!
        pdfTopic = intent.getStringExtra("pdfTopic")!!
        pdfUrl = intent.getStringExtra("pdfUrl")!!
        incrementViewCount()
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tvPdfTitle.text = pdfTopic
        binding.tvPdfTitle.isSelected = true
        MyApplication.loadPdfFromUrl(pdfUrl, binding.progressBar, binding.pdfView)

    }

    private fun incrementViewCount() {
        val ref = FirebaseDatabase.getInstance().getReference("Notes")
        ref.child(pdfId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var viewCount = snapshot.child("viewCount").value.toString()
                    if (viewCount == "" || viewCount == "null") {
                        viewCount = "0"
                    }
                    var newViewCount = viewCount.toLong() + 1

                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["viewCount"] = newViewCount

                    val dbref = FirebaseDatabase.getInstance().getReference("Notes")
                    dbref.child(pdfId)
                        .updateChildren(hashMap)
                        .addOnSuccessListener {
                            Log.d(PdfAdminAdapter.TAG, "onDataChange: Updated Downloads count ")
                        }
                        .addOnFailureListener {
                            Log.d(PdfAdminAdapter.TAG, "onDataChange: Download count Update Failed")
                        }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}