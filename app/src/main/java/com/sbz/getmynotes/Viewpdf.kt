package com.sbz.getmynotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sbz.getmynotes.application.MyApplication
import com.sbz.getmynotes.databinding.ActivityViewpdfBinding

class Viewpdf : AppCompatActivity() {

    private lateinit var binding: ActivityViewpdfBinding

    var pdfTopic = ""
    var pdfId = ""
    var pdfUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewpdf)

        pdfId = intent.getStringExtra("pdfId")!!
        pdfTopic = intent.getStringExtra("pdfTopic")!!
        pdfUrl = intent.getStringExtra("pdfUrl")!!

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tvPdfTitle.text = pdfTopic
        MyApplication.loadPdfFromUrl(pdfUrl, binding.progressBar, binding.pdfView)

    }
}