package com.sbz.getmynotes.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbz.getmynotes.R
import com.sbz.getmynotes.adapter.CourseListAdapter
import com.sbz.getmynotes.model.CourseList
import com.sbz.getmynotes.model.ModelSubjects


class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: CourseListAdapter
   // private lateinit var adapter: CourseListAdapter
    private lateinit var courseList: ArrayList<CourseList>
    private lateinit var data: ArrayList<CourseList>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        recyclerView = view.findViewById(R.id.rv_course_name)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //data = ArrayList()
        courseList = ArrayList()
        myAdapter = CourseListAdapter(requireContext(),courseList)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
        recyclerView.adapter = myAdapter
        getDataFromFirebase() // Call this function to get data from Firebase and update the adapter
    }

    private fun getDataFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Subjects")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newData = ArrayList<CourseList>()
                for (snapshot in dataSnapshot.children) {
                    val myData = snapshot.getValue(CourseList::class.java)
                    newData.add(myData!!)
                }
                courseList.clear()
                courseList.addAll(newData)
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException())
            }
        })
    }



}