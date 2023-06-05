package com.sbz.getmynotes.adapter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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
import com.sbz.getmynotes.filter.FilterUserPdf
import com.sbz.getmynotes.model.ModelPdf
import com.sbz.getmynotes.ui.Viewpdf
import java.io.FileOutputStream

class PdfUserAdapter(
    val context: Context,
    var pdfArrayList: ArrayList<ModelPdf>,
    /*options: FirebaseRecyclerOptions<ModelPdf>*/
) :
    /*FirebaseRecyclerAdapter<ModelPdf, PdfUserAdapter.ViewHolderUserPdf>(options),*/
    RecyclerView.Adapter<PdfUserAdapter.ViewHolderUserPdf>(),
    Filterable {

    private val filterList: ArrayList<ModelPdf> = pdfArrayList
    private var filter: FilterUserPdf? = null
    private lateinit var topic: String
    private lateinit var id: String
    private var progressBar: ProgressDialog? = null

    inner class ViewHolderUserPdf(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfTopicName: TextView = itemView.findViewById(R.id.pdf_topic_name_tv)
        val sizeTv: TextView = itemView.findViewById(R.id.sizeTv)
        val dateTv: TextView = itemView.findViewById(R.id.dateTv)
        val btnMore: ImageButton = itemView.findViewById(R.id.btn_more)
        val btnDownload: ImageButton = itemView.findViewById(R.id.btn_download)
        val totalDownloads: TextView = itemView.findViewById(R.id.tv_totalDownloads)
        val totalViews: TextView = itemView.findViewById(R.id.tv_totalViews)
        val btnRead: ImageButton = itemView.findViewById(R.id.btn_read_pdf)
        val progressBar: ProgressBar = itemView.findViewById(R.id.pdf_view_progress_bar)
        val pdfView: PDFView = itemView.findViewById(R.id.pdv_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserPdf {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pdf_admin, parent, false)
        return ViewHolderUserPdf(view)
    }

    /*override fun onBindViewHolder(holder: ViewHolderUserPdf, position: Int, model: ModelPdf) {
        val currentData = pdfArrayList[position]
        val uid=currentData.uid
        val subjectId=currentData.subjectId
        val url=currentData.url
        val viewCount=currentData.viewCount
        val downloadsCount=currentData.downloadsCount
        val timestamp = currentData.timestamp


        val modelPdf = ModelPdf(uid, id, topic, subjectId, url, timestamp, viewCount, downloadsCount)

        holder.btnRead.setOnClickListener {
            val intent = Intent(holder.btnRead.context, Viewpdf::class.java)
            intent.putExtra("filename", modelPdf.topic)
            intent.putExtra("fileurl", modelPdf.url)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.btnRead.context.startActivity(intent)
        }
    }*/

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderUserPdf, position: Int) {

        val currentData = pdfArrayList[position]
        val timestamp = currentData.timestamp
        topic = currentData.topic
        val pdfUrl = currentData.url
        id = currentData.id



        val formattedDate = MyApplication.formatTimeStamp(timestamp)
        holder.dateTv.text = formattedDate
        holder.pdfTopicName.text = topic
        holder.btnMore.visibility = View.GONE
        holder.totalDownloads.text = currentData.downloadsCount.toString()
        holder.totalViews.text = currentData.viewCount.toString()
        holder.btnDownload.setOnClickListener {
            downloadPdf(currentData)
        }




        MyApplication.loadPdfSize(pdfUrl, topic, holder.sizeTv)

       // val currentData = pdfArrayList[position]
        val uid=currentData.uid
        val subjectId=currentData.subjectId
        val url=currentData.url
        val viewCount=currentData.viewCount
        val downloadsCount=currentData.downloadsCount
        //val timestamp = currentData.timestamp


        val modelPdf = ModelPdf(uid, id, topic, subjectId, url, timestamp, viewCount, downloadsCount)

        holder.btnRead.setOnClickListener {
            val intent = Intent(holder.btnRead.context, Viewpdf::class.java)
            intent.putExtra("pdfTopic", modelPdf.topic)
            intent.putExtra("pdfId", id)
            intent.putExtra("pdfUrl", modelPdf.url)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.btnRead.context.startActivity(intent)
        }

        MyApplication.loadPdfFromUrlSinglePage(
            pdfUrl,
            topic,
            holder.progressBar,
            holder.pdfView,
            null
        )
    }



    private fun downloadPdf(currentData: ModelPdf) {
        val url = currentData.url

        progressBar = ProgressDialog(context)
        progressBar?.setMessage("Downloading ${currentData.topic}...")
        progressBar?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressBar?.max = 100
        progressBar?.setCancelable(false)
        progressBar?.show()

        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        ref.getBytes(5000000)
            .addOnSuccessListener { bytes ->
                saveToDownloads(bytes)
                progressBar?.dismiss()

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Downloading Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar?.dismiss()
            }
    }


//    private var isDownloading = true


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
            Log.d(PdfAdminAdapter.TAG, "saveToDownloads: ${e.printStackTrace()}")
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


    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterUserPdf(filterList, this)
        }
        return filter as FilterPdfAdmin
    }
}