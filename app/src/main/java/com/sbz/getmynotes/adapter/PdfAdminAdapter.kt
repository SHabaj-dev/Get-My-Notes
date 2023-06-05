package com.sbz.getmynotes.adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sbz.getmynotes.R
import com.sbz.getmynotes.application.MyApplication
import com.sbz.getmynotes.filter.FilterPdfAdmin
import com.sbz.getmynotes.model.ModelPdf
import com.sbz.getmynotes.ui.EditPdfActivity
import com.sbz.getmynotes.util.Constants
import java.io.FileOutputStream

class PdfAdminAdapter(val context: Context, var pdfArrayList: ArrayList<ModelPdf>) :
    RecyclerView.Adapter<PdfAdminAdapter.ViewHolderAdminPdf>(),
    Filterable {

    companion object {
        const val TAG = "TITLE_EXCEPTION_ERROR"
    }

    private var progressDialog: ProgressDialog? = null

    private val filterList: ArrayList<ModelPdf> = pdfArrayList
    private var filter: FilterPdfAdmin? = null
    private lateinit var topic: String
    private lateinit var id: String


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAdminPdf {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pdf_admin, parent, false)
        return ViewHolderAdminPdf(view)
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderAdminPdf, position: Int) {
        val currentData = pdfArrayList[position]
        val timestamp = currentData.timestamp
        topic = currentData.topic
        val pdfUrl = currentData.url
        id = currentData.id

        val formattedDate = MyApplication.formatTimeStamp(timestamp)
        holder.dateTv.text = formattedDate
        holder.pdfTopicNameTv.text = topic
        holder.totalDownload.text = currentData.downloadsCount.toString()
        holder.totalViews.text = currentData.viewCount.toString()

//        MyApplication.loadSubject(subjectId, holder.subjectTv)

//        Need to check this Not working!!
        MyApplication.loadPdfFromUrlSinglePage(pdfUrl, topic, holder.progressBar, holder.pdfView, null)
        MyApplication.loadPdfSize(pdfUrl, topic, holder.sizeTv)

        holder.btnMore.setOnClickListener {
            moreOptionDialogue(currentData, holder)
        }

        holder.btnDownload.setOnClickListener {

            downloadPdf(currentData, holder)

        }


    }


    private fun downloadPdf(currentData: ModelPdf, holder: ViewHolderAdminPdf) {
        val url = currentData.url

        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage("Downloading ${currentData.topic}...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()


        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        ref.getBytes(Constants.MAX_BYTES_PDF)
            .addOnSuccessListener { bytes ->
                saveToDownloads(bytes)

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Downloading Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    private fun saveToDownloads(bytes: ByteArray?) {
        val nameWithExtension = "$topic.pdf"

        try {
            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            downloadsFolder.mkdirs()

            val filePath = downloadsFolder.path + "/" + nameWithExtension
            val out = FileOutputStream(filePath)
            out.write(bytes)
            out.close()
            Toast.makeText(context, "$topic is downloaded successfully", Toast.LENGTH_SHORT)
                .show()
            incrementDownloadCount()

        } catch (e: Exception) {
            Log.d(TAG, "saveToDownloads: ${e.printStackTrace()}")
        }
    }

    private fun incrementDownloadCount() {
        val ref = FirebaseDatabase.getInstance().getReference("Notes")
        ref.child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var downloadCount = snapshot.child("downloadsCount").value.toString()
                    if (downloadCount == "" || downloadCount == "null") {
                        downloadCount = "0"
                    }
                    var newDownloadCount = downloadCount.toLong() + 1

                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["downloadsCount"] = newDownloadCount

                    val dbref = FirebaseDatabase.getInstance().getReference("Notes")
                    dbref.child(id)
                        .updateChildren(hashMap)
                        .addOnSuccessListener {
                            Log.d(TAG, "onDataChange: Updated Downloads count ")
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "onDataChange: Download count Update Failed")
                        }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }


    private fun moreOptionDialogue(
        currentData: ModelPdf,
        holder: PdfAdminAdapter.ViewHolderAdminPdf
    ) {
        val subjectId = currentData.subjectId
        topic = currentData.topic
        val url = currentData.url

        val options = arrayOf("Edit", "Delete")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option")
            .setItems(options) { dialog, position ->
                if (position == 0) {
                val intent = Intent(context, EditPdfActivity::class.java)
                    intent.putExtra("subjectId", currentData.id)
                    context.startActivity(intent)
                } else {

                    MyApplication.deleteNotesPdf(context, currentData.id, url, topic)
                }

            }
            .show()


    }


    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterPdfAdmin(filterList, this)

        }
        return filter as FilterPdfAdmin
    }


    inner class ViewHolderAdminPdf(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val pdfTopicNameTv: TextView = itemView.findViewById(R.id.pdf_topic_name_tv)
        val sizeTv: TextView = itemView.findViewById(R.id.sizeTv)
        val dateTv: TextView = itemView.findViewById(R.id.dateTv)
        val btnMore: ImageButton = itemView.findViewById(R.id.btn_more)
        val btnDownload: ImageButton = itemView.findViewById(R.id.btn_download)
        val totalDownload: TextView = itemView.findViewById(R.id.tv_totalDownloads)
        val totalViews: TextView = itemView.findViewById(R.id.tv_totalViews)
        val progressBar: ProgressBar = itemView.findViewById(R.id.pdf_view_progress_bar)
        val pdfView: PDFView = itemView.findViewById(R.id.pdv_view)
    }


}