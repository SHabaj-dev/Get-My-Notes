package com.sbz.getmynotes.filter

import android.widget.Filter
import com.sbz.getmynotes.adapter.PdfAdminAdapter
import com.sbz.getmynotes.model.ModelPdf

class FilterPdfAdmin(var filterList: ArrayList<ModelPdf>, var adapterPdfAmin: PdfAdminAdapter) :
    Filter() {


    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint
        val results = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().lowercase()
            var filteredModel = ArrayList<ModelPdf>()
            for (i in filterList.indices) {
                if (filterList[i].topic.lowercase().contains(constraint)) {
                    filteredModel.add(filterList[i])
                }
            }
            results.count = filteredModel.size
            results.values = filteredModel

        } else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

        adapterPdfAmin.pdfArrayList = results!!.values as ArrayList<ModelPdf>
        adapterPdfAmin.notifyDataSetChanged()

    }


}