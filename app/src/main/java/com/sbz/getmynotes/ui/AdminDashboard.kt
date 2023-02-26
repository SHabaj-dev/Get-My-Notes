package com.sbz.getmynotes.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbz.getmynotes.R
import com.sbz.getmynotes.adapter.CourseListAdapter
import com.sbz.getmynotes.adapter.SubjectListAdapter
import com.sbz.getmynotes.databinding.ActivityAdminDashboardBinding
import com.sbz.getmynotes.model.CourseList
import com.sbz.getmynotes.model.ModelSubjects

class AdminDashboard : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: SubjectListAdapter
    private lateinit var subjectList: ArrayList<ModelSubjects>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard)
        subjectList = ArrayList()
        mAuth = FirebaseAuth.getInstance()
        checkUser()

        val ref = FirebaseDatabase.getInstance().getReference("Subjects")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                subjectList.clear()
                for(ds in snapshot.children){
                    val subject = ds.getValue(ModelSubjects::class.java)
                    subject?.let{subjectList.add(it)}
                }
//                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        )

        recyclerView = findViewById(R.id.rv_course_name)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        myAdapter = SubjectListAdapter(this, subjectList)
        recyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()

        binding.btnLogoutAdmin.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, LoginPage::class.java))
            finish()
            checkUser()
        }


        binding.btnAddSubject.setOnClickListener {
            startActivity(Intent(this, AddSubjectActivity::class.java))
        }


    }

    private fun checkUser() {
        val firebaseUser = mAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        } else {
            val mEmail = firebaseUser.email
            binding.tvAdminText.text = mEmail
        }
    }
}