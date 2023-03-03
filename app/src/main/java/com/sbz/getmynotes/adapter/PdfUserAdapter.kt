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
import com.sbz.getmynotes.filter.FilterUserPdf
import com.sbz.getmynotes.model.ModelPdf

class PdfUserAdapter(
    val context: Context,
    var pdfArrayList: ArrayList<ModelPdf>
) :
    RecyclerView.Adapter<PdfUserAdapter.ViewHolderUserPdf>(),
    Filterable {

    private val filterList: ArrayList<ModelPdf> = pdfArrayList
    private var filter: FilterUserPdf? = null

    inner class ViewHolderUserPdf(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfTopicName: TextView = itemView.findViewById(R.id.pdf_topic_name_tv)
        val sizeTv: TextView = itemView.findViewById(R.id.sizeTv)
        val dateTv: TextView = itemView.findViewById(R.id.dateTv)
        val btnMore: ImageButton = itemView.findViewById(R.id.btn_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserPdf {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pdf_admin, parent, false)
        return ViewHolderUserPdf(view)
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderUserPdf, position: Int) {
        val currentData = pdfArrayList[position]
        val timestamp = currentData.timestamp
        val topic = currentData.topic
        val pdfUrl = currentData.url
        val subjectId = currentData.subjectId

        val formattedDate = MyApplication.formatTimeStamp(timestamp)
        holder.dateTv.text = formattedDate
        holder.pdfTopicName.text = topic

        MyApplication.loadPdfSize(pdfUrl, topic, holder.sizeTv)
        MyApplication.loadPdfFromUrlSinglePAge(pdfUrl, topic, null)
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterUserPdf(filterList, this)
        }
        return filter as FilterPdfAdmin
    }
}