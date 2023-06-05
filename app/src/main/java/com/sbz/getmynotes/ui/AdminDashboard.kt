package com.sbz.getmynotes.ui

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbz.getmynotes.R
import com.sbz.getmynotes.adapter.AdminSubjectAdapter
import com.sbz.getmynotes.databinding.ActivityAdminDashboardBinding
import com.sbz.getmynotes.model.AdminSubjectModel

class AdminDashboard : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: AdminSubjectAdapter
    private lateinit var subjectList: ArrayList<AdminSubjectModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard)

        val window: Window = window
        val decorView: View = window.decorView
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true
        window.statusBarColor = getColor(R.color.bar_color)

        subjectList = ArrayList()
        mAuth = FirebaseAuth.getInstance()
        checkUser()

        recyclerView = findViewById(R.id.rv_course_name)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
        myAdapter = AdminSubjectAdapter(this, subjectList)
        recyclerView.adapter = myAdapter

        getDataFromFirebase()

        binding.fbAddPdf.setOnClickListener {
            startActivity(Intent(this@AdminDashboard, AddPdfActivity::class.java))
        }

        binding.btnLogoutAdmin.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, LoginPage::class.java))
            finish()
            checkUser()
        }


        binding.btnAddSubject.setOnClickListener {
            startActivity(Intent(this, AddSubjectActivity::class.java))
        }

        binding.etSearchCourses.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    myAdapter.filter!!.filter(s)
                } catch (e: Exception) {
                    Log.d("SEARCH_LOG", "onTextChanged: ${e.message}")
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
                val newData = ArrayList<AdminSubjectModel>()
                for (snapshot in dataSnapshot.children) {
                    val myData = snapshot.getValue(AdminSubjectModel::class.java)
                    myData?.timestamp = snapshot.child("timestamp").value.toString().toLong()
                    newData.add(myData!!)
                }
                subjectList.clear()
                subjectList.addAll(newData)
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "onCancelled", databaseError.toException())
            }
        })
    }

    private fun checkUser() {
        val firebaseUser = mAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        } else {
            val mEmail = firebaseUser.email
//            binding.tvAdminText.text = mEmail
        }
    }
}