package com.sbz.getmynotes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbz.getmynotes.R
import com.sbz.getmynotes.adapter.PdfUserAdapter
import com.sbz.getmynotes.model.ModelPdf


class PdfViewUserFragment : Fragment(R.layout.fragment_pdf_view_user) {


    private lateinit var pdfArrayList: ArrayList<ModelPdf>
    private lateinit var myAdapter: PdfUserAdapter
    private lateinit var searchText: TextView
    private lateinit var recyclerView: RecyclerView
    private var subjectId: String = ""
    private lateinit var firebaseRecyclerOptions: FirebaseRecyclerOptions<ModelPdf>
//    private var subjectName: String = ""
    private lateinit var mAdapter: FirebaseRecyclerAdapter<ModelPdf, PdfUserAdapter.ViewHolderUserPdf>
    private val mRecyclerView: RecyclerView? = null


    companion object {
        fun newInstance(subjectId: String): PdfViewUserFragment {
            val fragment = PdfViewUserFragment()
            val args = Bundle()
            args.putString("user_subject_model", subjectId)
//            args.putString("User_subject_name", subjectName)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pdf_view_user, container, false)
        recyclerView = view.findViewById(R.id.rv_course_name_user)
        searchText = view.findViewById(R.id.et_search_courses_user)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPdfList()

    }

    private fun loadPdfList() {
        pdfArrayList = ArrayList()
        subjectId = arguments?.getString("user_subject_model").toString().trim()
        //firebaseRecyclerOptions=

       /* if (!isAdded) {
            return
        }*/

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
                    recyclerView.hasFixedSize()
                    recyclerView.layoutManager =
                        StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
                    myAdapter = PdfUserAdapter(requireContext(), pdfArrayList)
                    recyclerView.adapter = myAdapter
                    myAdapter.notifyDataSetChanged()

//                    adapterPdfAdmin = PdfAdminAdapter(this@PdfListActivity, pdfArrayList)
//                    binding.rvCourseName.adapter = adapterPdfAdmin
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

   /* override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }*/


}