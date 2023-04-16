package com.sbz.getmynotes.application

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sbz.getmynotes.util.Constants.MAX_BYTES_PDF
import java.util.Calendar
import java.util.Locale

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

        fun loadPdfFromUrl(
            pdfUrl: String,
            progressBar: ProgressBar,
            pdfView: PDFView
        ) {
            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
            ref.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener { bytes ->
                    pdfView.fromBytes(bytes)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .spacing(10)
                        .onError { t ->
                            progressBar.visibility = View.INVISIBLE
                            Log.d(TAG, "loadPdfFromUrlSinglePage: ${t.message}")
                        }
                        .onPageError { page, t ->
                            progressBar.visibility = View.INVISIBLE
                            Log.d(TAG, "loadPdfFromUrlSinglePage: ${t.message}")
                        }
                        .onLoad { t ->
                            progressBar.visibility = View.INVISIBLE
//                            pagesTv?.text = "Pages: ${t}"
                        }
                        .load()
                }
                .addOnFailureListener { exception ->
                    // Handle any errors here
                    Log.e(TAG, "Failed to load PDF: ${exception.message}")
                }
        }


        fun loadPdfFromUrlSinglePage(
            pdfUrl: String,
            pdfTitle: String,
            progressBar: ProgressBar,
            pdfView: PDFView,
            pagesTv: TextView?
        ) {
            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
            ref.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener { bytes ->

                    pdfView.fromBytes(bytes)
                        .defaultPage(0)
                        .spacing(0)
                        .swipeHorizontal(false)
                        .enableSwipe(false)
                        .onError { t ->
                            progressBar.visibility = View.INVISIBLE
                            Log.d(TAG, "loadPdfFromUrlSinglePage: ${t.message}")
                        }
                        .onPageError { page, t ->
                            progressBar.visibility = View.INVISIBLE
                            Log.d(TAG, "loadPdfFromUrlSinglePage: ${t.message}")
                        }
                        .onLoad { t ->
                            progressBar.visibility = View.INVISIBLE
                            pagesTv?.text = "Pages: ${t}"
                        }
                        .load()


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


        fun deleteNotesPdf(context: Context, subject: String, url: String, topic: String) {
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Please Wait")
            progressDialog.setMessage("Deleting Notes $topic...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            val storeRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
            storeRef.delete()
                .addOnSuccessListener {

                    val ref = FirebaseDatabase.getInstance().getReference("Notes")
                    ref.child(subject)
                        .removeValue()
                        .addOnSuccessListener {
                            Log.d(TAG, "deleteNotesPdf: Removed Data From DB")
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "deleteNotesPdf: Failed Deleting from DB")
                        }


                    progressDialog.dismiss()
                    Toast.makeText(context, "Deletion Successfully...", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Deletion Failed due to ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }


    }
}