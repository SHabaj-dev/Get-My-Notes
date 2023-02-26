package com.sbz.getmynotes.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sbz.getmynotes.R
import com.sbz.getmynotes.adapter.CourseListAdapter
import com.sbz.getmynotes.model.CourseList


class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val courseList = mutableListOf(
            CourseList("MCA"),
            CourseList("B,Tech(CSE)"),
            CourseList("B,Tech(ME)"),
            CourseList("B,Tech(EE)"),
            CourseList("BE"),
        )

        recyclerView = view.findViewById(R.id.rv_course_name)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        val myAdpater = CourseListAdapter(requireContext(), courseList)
        recyclerView.adapter = myAdpater

    }



}