package com.sbz.getmynotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sbz.getmynotes.R
import com.sbz.getmynotes.application.MyApplication
import com.sbz.getmynotes.filter.FilterPdfAdmin
import com.sbz.getmynotes.model.ModelPdf

class PdfAdminAdapter(val context: Context, var pdfArrayList: ArrayList<ModelPdf>) :
    RecyclerView.Adapter<PdfAdminAdapter.ViewHolderAdminPdf>(),
    Filterable {

    private val filterList: ArrayList<ModelPdf> = pdfArrayList


    var filter: FilterPdfAdmin? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAdminPdf {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pdf_admin, parent, false)
        return ViewHolderAdminPdf(view)
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderAdminPdf, position: Int) {
        val currentData = pdfArrayList[position]
        val pdfId = currentData.id
        val subjectId = currentData.subjectId
        val timestamp = currentData.timestamp
        val topic = currentData.topic
        val uid = currentData.uid
        val pdfUrl = currentData.url
        val downloadCount = currentData.downloadsCount
        val viewCount = currentData.viewCount

        val formattedDate = MyApplication.formatTimeStamp(timestamp)
        holder.dateTv.text = formattedDate
        holder.pdfTopicNameTv.text = topic

//        MyApplication.loadSubject(subjectId, holder.subjectTv)
        MyApplication.loadPdfFromUrlSinglePAge(pdfUrl, topic, null)
        MyApplication.loadPdfSize(pdfUrl, topic, holder.sizeTv)


    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterPdfAdmin(filterList, this)

        }
        return filter as FilterPdfAdmin
    }


    inner class ViewHolderAdminPdf(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val pdfTopicNameTv = itemView.findViewById<TextView>(R.id.pdf_topic_name_tv)
        val sizeTv = itemView.findViewById<TextView>(R.id.sizeTv)
        val dateTv = itemView.findViewById<TextView>(R.id.dateTv)
        val btnMore = itemView.findViewById<ImageButton>(R.id.btn_more)
    }


}