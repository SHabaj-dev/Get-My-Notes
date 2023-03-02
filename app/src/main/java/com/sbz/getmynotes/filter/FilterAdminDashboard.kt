package com.sbz.getmynotes.filter

import android.widget.Filter
import com.sbz.getmynotes.adapter.AdminSubjectAdapter
import com.sbz.getmynotes.model.AdminSubjectModel

class FilterAdminDashboard(
    var filterList: ArrayList<AdminSubjectModel>,
    var adapterSubjectAdmin: AdminSubjectAdapter
) :
    Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint
        val results = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().lowercase()
            var filteredModel = ArrayList<AdminSubjectModel>()
            for (i in filterList.indices) {
                if (filterList[i].subject.lowercase().contains(constraint)) {
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

        adapterSubjectAdmin.subjectList = results!!.values as ArrayList<AdminSubjectModel>
        adapterSubjectAdmin.notifyDataSetChanged()
    }
}