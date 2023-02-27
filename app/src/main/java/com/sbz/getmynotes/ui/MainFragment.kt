package com.sbz.getmynotes.ui

import android.os.Bundle
import android.view.View
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
    private lateinit var courseList: ArrayList<CourseList>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Subjects")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                courseList.clear()
                for(ds in snapshot.children){
                    val course = ds.getValue(CourseList::class.java)
                    course?.let{courseList.add(it)}
                }
//                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        )


//        val courseList = mutableListOf(
//            CourseList("MCA"),
//            CourseList("B,Tech(CSE)"),
//            CourseList("B,Tech(ME)"),
//            CourseList("B,Tech(EE)"),
//            CourseList("BE"),
//        )

        recyclerView = view.findViewById(R.id.rv_course_name)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        val myAdpater = CourseListAdapter(requireContext(), courseList)
        recyclerView.adapter = myAdpater

    }



}