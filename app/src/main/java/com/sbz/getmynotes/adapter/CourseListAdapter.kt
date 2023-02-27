package com.sbz.getmynotes.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sbz.getmynotes.R
import com.sbz.getmynotes.model.CourseList

class CourseListAdapter(val context: Context, val courseList: List<CourseList>) :
    RecyclerView.Adapter<CourseListAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val cardLayout = itemView.findViewById<CardView>(R.id.card_layout)
        val courseName: TextView = itemView.findViewById(R.id.list_item_course)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_custom, parent, false)
        return CourseViewHolder(view)
    }


    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {

        holder.courseName.text = courseList[position].subject
    }
}