package com.sbz.getmynotes.filter

import android.widget.Filter
import com.sbz.getmynotes.adapter.UserSubjectAdapter
import com.sbz.getmynotes.model.UserSubjectModel

class FilterCourseUser(
    var filterList: ArrayList<UserSubjectModel>,
    var adapter: UserSubjectAdapter
) :
    Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint
        val results = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().lowercase()
            var filteredModel = ArrayList<UserSubjectModel>()
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

        adapter.userSubjectModel = results!!.values as ArrayList<UserSubjectModel>
        adapter.notifyDataSetChanged()
    }
}