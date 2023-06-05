package com.sbz.getmynotes.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbz.getmynotes.R
import com.sbz.getmynotes.adapter.PdfAdminAdapter
import com.sbz.getmynotes.adapter.PdfUserAdapter
import com.sbz.getmynotes.databinding.ActivityPdfListBinding
import com.sbz.getmynotes.model.ModelPdf

class PdfListActivity : AppCompatActivity() {

    private var subjectId = ""
    private var subject = ""
    private lateinit var binding: ActivityPdfListBinding
    private lateinit var pdfArrayList: ArrayList<ModelPdf>
    private lateinit var adapterPdfAdmin: PdfAdminAdapter
    private val TAG = "PDF_LIST_ACTIVITY"

    var recview: RecyclerView? = null
    var adapter: PdfUserAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        subjectId = intent.getStringExtra("subjectId")!!
        subject = intent.getStringExtra("subject")!!


        binding.subjectName.text = subject

        loadPdfList()


        binding.btnBackPdfAdmin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }




        binding.etSearchCourses.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterPdfAdmin.filter!!.filter(s)

                } catch (e: Exception) {
                    Log.d(TAG, "onTextChanged: ${e.message}")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


        recview = findViewById<View>(R.id.rv_course_name) as RecyclerView
        recview!!.layoutManager = LinearLayoutManager(this)
        val options = FirebaseRecyclerOptions.Builder<ModelPdf>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("notes"),
                ModelPdf::class.java
            )
            .build()
        adapter = PdfUserAdapter(applicationContext, pdfArrayList)
        recview!!.adapter = adapter


    }

    private fun loadPdfList() {
        pdfArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Notes")
        ref.orderByChild("subjectId").equalTo(subjectId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for (ds in snapshot.children) {
                        val model = ds.getValue(ModelPdf::class.java)
                        if (model != null) {
                            pdfArrayList.add(model)
                        }
                    }
                    binding.rvCourseName.hasFixedSize()
                    binding.rvCourseName.layoutManager =
                        StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
                    adapterPdfAdmin = PdfAdminAdapter(this@PdfListActivity, pdfArrayList)
                    binding.rvCourseName.adapter = adapterPdfAdmin
                    adapterPdfAdmin.notifyDataSetChanged()

//                    adapterPdfAdmin = PdfAdminAdapter(this@PdfListActivity, pdfArrayList)
//                    binding.rvCourseName.adapter = adapterPdfAdmin
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}