package com.sbz.getmynotes.adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.sbz.getmynotes.ui.EditPdfActivity
import com.sbz.getmynotes.R
import com.sbz.getmynotes.application.MyApplication
import com.sbz.getmynotes.filter.FilterPdfAdmin
import com.sbz.getmynotes.model.ModelPdf

class PdfAdminAdapter(val context: Context, var pdfArrayList: ArrayList<ModelPdf>) :
    RecyclerView.Adapter<PdfAdminAdapter.ViewHolderAdminPdf>(),
    Filterable {

    private val filterList: ArrayList<ModelPdf> = pdfArrayList


    private var filter: FilterPdfAdmin? = null


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
        val topic = currentData.topic
        val pdfUrl = currentData.url

        val formattedDate = MyApplication.formatTimeStamp(timestamp)
        holder.dateTv.text = formattedDate
        holder.pdfTopicNameTv.text = topic

//        MyApplication.loadSubject(subjectId, holder.subjectTv)
        MyApplication.loadPdfFromUrlSinglePAge(pdfUrl, topic, null)
        MyApplication.loadPdfSize(pdfUrl, topic, holder.sizeTv)

        holder.btnMore.setOnClickListener {
            moreOptionDialogue(currentData, holder)
        }


    }

    private fun moreOptionDialogue(
        currentData: ModelPdf,
        holder: PdfAdminAdapter.ViewHolderAdminPdf
    ) {
        val subjectId = currentData.subjectId
        val topic = currentData.topic
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
    }


}