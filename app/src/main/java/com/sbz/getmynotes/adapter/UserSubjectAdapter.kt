package com.sbz.getmynotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sbz.getmynotes.R
import com.sbz.getmynotes.filter.FilterCourseUser
import com.sbz.getmynotes.model.UserSubjectModel

class UserSubjectAdapter(val context: Context, var userSubjectModel: ArrayList<UserSubjectModel>) :
    RecyclerView.Adapter<UserSubjectAdapter.CourseViewHolder>(),
    Filterable {

    private val filterList: ArrayList<UserSubjectModel> = userSubjectModel
    private var filter: FilterCourseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_custom, parent, false)
        return CourseViewHolder(view)
    }


    override fun getItemCount(): Int {
        return userSubjectModel.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {

        holder.courseName.text = userSubjectModel[position].subject
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterCourseUser(filterList, this)
        }
        return filter as FilterCourseUser
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val cardLayout = itemView.findViewById<CardView>(R.id.card_layout)
        val courseName: TextView = itemView.findViewById(R.id.list_item_course)
    }}