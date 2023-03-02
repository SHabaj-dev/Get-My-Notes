package com.sbz.getmynotes.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.sbz.getmynotes.R
import com.sbz.getmynotes.ui.LoginPage

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val updatedUserName = view.findViewById<TextView>(R.id.tv_user_name_settings)
        val updateButton = view.findViewById<AppCompatButton>(R.id.btn_update_profile)
        val logOutButton = view.findViewById<AppCompatButton>(R.id.btn_logout)


        updateButton.setOnClickListener {
            val newUName = updatedUserName.text.toString()
            updateUserInfo(newUName)

        }


        logOutButton.setOnClickListener {
            logOutCurrentUser()
        }

    }

    private fun logOutCurrentUser() {
        Firebase.auth.signOut()
        Toast.makeText(requireContext(), "Successfully Logged Out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(requireContext(), LoginPage::class.java))
        requireActivity().finish()


    }




    private fun updateUserInfo(updatedUserName: String) {
        val user = Firebase.auth.currentUser


        val profileUpdates = userProfileChangeRequest {
            displayName = updatedUserName
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Updated Profile Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Updating Profile Failed!! Try Again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
