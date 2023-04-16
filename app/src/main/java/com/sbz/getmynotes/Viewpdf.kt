package com.sbz.getmynotes

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import java.net.URLEncoder

class Viewpdf : AppCompatActivity() {
    var pdfview: WebView? = null

    var pd: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpdf)

        pdfview = findViewById<View>(R.id.viewpdf) as WebView
        pdfview!!.settings.javaScriptEnabled = true
        val filename = intent.getStringExtra("filename")
        val fileurl = intent.getStringExtra("fileurl")
        val pd = ProgressDialog(this)
        pd.setTitle(filename)
        pd.setMessage("Opening....!!!")
        pdfview!!.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                //pd.show()
                if (favicon != null) {
                    pd!!.show()
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                pd.dismiss()
            }
        }
        var url = ""
        try {
            url = URLEncoder.encode(fileurl, "UTF-8")
        } catch (_: Exception) {
        }
        pdfview!!.loadUrl("http://docs.google.com/gview?embedded=true&url=$url")


    }
}