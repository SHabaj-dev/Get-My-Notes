package com.sbz.getmynotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.sbz.getmynotes.R
import com.sbz.getmynotes.model.ModelSubjects

class SubjectListAdapter(val context: Context, val subjectList: List<ModelSubjects>) :
    RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        //        val cardLayout = itemView.findViewById<CardView>(R.id.card_layout)
        val listItemSubject = itemView.findViewById<TextView>(R.id.list_item_subject)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_custom_admin, parent, false)
        return SubjectViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subjectList.size
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val currentCourse = subjectList[position]
        val id = currentCourse.id
        val subject = currentCourse.subject
        val uid = currentCourse.uid
        val timeStamp = currentCourse.timestamp

        holder.listItemSubject.text = subject
        holder.deleteButton.setOnClickListener {
            deleteCategory(currentCourse, holder)
        }
    }

    private fun deleteCategory(currentCourse: ModelSubjects, holder: SubjectViewHolder) {
        val id = currentCourse.id
//        Subjects -> subjectId
        val ref = FirebaseDatabase.getInstance().getReference("Subjects")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deletion Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error ${e.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }
    }
}