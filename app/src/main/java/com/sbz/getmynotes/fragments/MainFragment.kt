package com.sbz.getmynotes.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbz.getmynotes.R
import com.sbz.getmynotes.adapter.UserSubjectAdapter
import com.sbz.getmynotes.model.UserSubjectModel


class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: UserSubjectAdapter
   // private lateinit var adapter: CourseListAdapter
    private lateinit var userSubjectModel: ArrayList<UserSubjectModel>
    private lateinit var data: ArrayList<UserSubjectModel>
    private lateinit var searchText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        recyclerView = view.findViewById(R.id.rv_course_name)
        searchText = view.findViewById(R.id.sv_search_notes_main)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //data = ArrayList()
        userSubjectModel = ArrayList()
        myAdapter = UserSubjectAdapter(requireContext(), userSubjectModel)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
        recyclerView.adapter = myAdapter
        getDataFromFirebase() // Call this function to get data from Firebase and update the adapter

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    myAdapter.filter!!.filter(s)

                } catch (e: Exception) {
                    Log.d(TAG, "onTextChanged: ${e.message}")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun getDataFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Subjects")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newData = ArrayList<UserSubjectModel>()
                for (snapshot in dataSnapshot.children) {
                    val myData = snapshot.getValue(UserSubjectModel::class.java)
                    newData.add(myData!!)
                }
                userSubjectModel.clear()
                userSubjectModel.addAll(newData)
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException())
            }
        })
    }



}