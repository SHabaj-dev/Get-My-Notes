package com.sbz.getmynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class ContactUs : AppCompatActivity() {
    private lateinit var backBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        backBtn = findViewById(R.id.btn_back_cus)

        backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}