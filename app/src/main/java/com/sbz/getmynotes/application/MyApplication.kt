package com.sbz.getmynotes.application

import android.app.Application
import android.text.format.DateFormat
import android.util.Log
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        val TAG = "PDF_SIZE_TAG"
        fun formatTimeStamp(timestamp: Long): String {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timestamp

            return DateFormat.format("dd/MM/yyyy", cal).toString()
        }

        fun loadPdfSize(pdfUrl: String, pdfTitle: String, sizeTv: TextView) {


            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
            ref.metadata
                .addOnSuccessListener { storageMetaData ->
                    val bytes = storageMetaData.sizeBytes.toDouble()

                    val kb = bytes / 1024
                    val mb = kb / 1024
                    if (mb >= 1) {
                        sizeTv.text = "${String.format("%.2f", mb)} MB"
                    } else if (kb >= 1) {
                        sizeTv.text = "${String.format("%.2f", kb)} KB"
                    } else {
                        sizeTv.text = "${String.format("%.2f", bytes)} bytes"
                    }

                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "loadPdfSize: ${e.message}")
                }
        }


        fun loadPdfFromUrlSinglePAge(pdfUrl: String, pdfTitle: String, pagesTv: TextView?) {
            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
            ref.metadata
                .addOnSuccessListener { storageMetaData ->
                    val bytes = storageMetaData.sizeBytes.toDouble()


                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "loadPdfSize: ${e.message}")
                }
        }

        fun loadSubject(subjectId: String, subjectTv: TextView) {
            val ref = FirebaseDatabase.getInstance().getReference("Subjects")
            ref.child(subjectId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val subject: String = snapshot.child("subject").value.toString()

                        subjectTv.text = subject

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }


    }
}