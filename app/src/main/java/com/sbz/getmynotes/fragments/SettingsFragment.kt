package com.sbz.getmynotes.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.sbz.getmynotes.ContactUs
import com.sbz.getmynotes.R
import com.sbz.getmynotes.ui.LoginPage
import com.sbz.getmynotes.ui.UpdateProfileActivity

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userName: EditText
    private lateinit var universityName: EditText
    private var uName: String = ""
    private var uniName: String = ""
    private lateinit var userDP: ImageView
    private lateinit var contactUs: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = view.findViewById(R.id.tv_user_name_settings)
        universityName = view.findViewById(R.id.tv_collage_name_settings)
        userDP = view.findViewById(R.id.iv_settings_user)
        contactUs = view.findViewById(R.id.tv_send_notes)
        val updateButton = view.findViewById<AppCompatButton>(R.id.btn_update_profile)
        val logOutButton = view.findViewById<AppCompatButton>(R.id.btn_logout)

        /*val shapeApperanceModel = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, 225f)
            .build()
        userDP.shapeAppearanceModel = shapeApperanceModel*/

        mAuth = FirebaseAuth.getInstance()
        getDataFromDB()
        //Handle click for update button
        updateButton.setOnClickListener {
            val intent = Intent(activity, UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        contactUs.setOnClickListener {
            launchContactUsActivity()
        }


        logOutButton.setOnClickListener {
            logOutCurrentUser()
        }

    }

    private fun launchContactUsActivity() {
        val intent = Intent(requireContext(), ContactUs::class.java)
        startActivity(intent)
    }


    private fun getDataFromDB() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(mAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    uName = snapshot.child("userName").value.toString()
                    uniName = snapshot.child("university").value.toString()
                    userName.setText(uName)
                    universityName.setText(uniName)
                    val profileImage = snapshot.child("profileImage").value.toString()

                    try {
                        Glide
                            .with(this@SettingsFragment)
                            .load(profileImage)
                            .placeholder(R.drawable.person_icon)
                            .centerCrop()
                            .into(userDP)

                    } catch (e: Exception) {
                        Log.d("DP_LOADING", "onDataChange: ${e.message}")
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }


    private fun logOutCurrentUser() {
        Firebase.auth.signOut()
        Toast.makeText(requireContext(), "Successfully Logged Out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(requireContext(), LoginPage::class.java))
        requireActivity().finish()


    }


}
