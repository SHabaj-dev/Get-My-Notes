package com.sbz.getmynotes.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sbz.getmynotes.R
import com.sbz.getmynotes.databinding.ActivityUpdateProfileBinding

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var mAuth: FirebaseAuth
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*val shapeApperanceModel = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, 225f)
            .build()
        binding.shapeableImageView.shapeAppearanceModel = shapeApperanceModel*/

        mAuth = FirebaseAuth.getInstance()
        loadUserInfo()



        binding.ibBackBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnUpdateInfo.setOnClickListener {
            validateData()
        }
        binding.roundImageView.setOnClickListener {
            showImageAttachMenu()

        }

    }


    private var name = ""
    private var uniName = ""
    private fun validateData() {
        name = binding.etName.text.toString().trim()
        uniName = binding.etUniName.text.toString().trim()
        if (name.isEmpty()) {
            Toast.makeText(this, "User Name Can't Be Empty", Toast.LENGTH_SHORT).show()
        } else if (uniName.isEmpty()) {
            Toast.makeText(this, "University Name can't be Empty", Toast.LENGTH_SHORT).show()
        } else {
            if (imageUri == null) {
                updateProfile("")
            } else {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvPleaseWait.visibility = View.VISIBLE

        val filePathAndName = "ProfileImages/" + mAuth.uid

        val storageRef = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);

                val uploadedImageUrl = uriTask.result.toString()

                updateProfile(uploadedImageUrl)



                Toast.makeText(this, " Uploaded Successfully.", Toast.LENGTH_SHORT)
                    .show()
                binding.progressBar.visibility = View.GONE
                binding.tvPleaseWait.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(this, "Uploading Failed Due to ${it.message}", Toast.LENGTH_SHORT)
                    .show()
                binding.progressBar.visibility = View.GONE
                binding.tvPleaseWait.visibility = View.GONE
            }
    }

    private fun updateProfile(uploadedImageUri: String) {

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["userName"] = name
        hashMap["university"] = uniName
        if (imageUri != null) {
            hashMap["profileImage"] = uploadedImageUri
        }

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(mAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, " Update Successfully.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update Failed due to ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    private fun showImageAttachMenu() {

        val popupMenu = PopupMenu(this, binding.roundImageView)
        popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            val id = item.itemId
            if (id == 0) {
                pickImageCamera()
            } else if (id == 1) {
                pickImageGallery()
            }


            true
        }

    }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActionResultLauncher.launch(intent)
    }

    private val cameraActionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
//                imageUri = data!!.data

                binding.roundImageView.setImageURI(imageUri)
            } else {
                Toast.makeText(this@UpdateProfileActivity, "Cancelled", Toast.LENGTH_SHORT).show()
            }

        }
    )


    private fun pickImageGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActionResultLauncher.launch(intent)

    }

    private val galleryActionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data!!.data

                binding.roundImageView.setImageURI(imageUri)
            } else {
                Toast.makeText(this@UpdateProfileActivity, "Cancelled", Toast.LENGTH_SHORT).show()
            }

        }
    )


    private fun loadUserInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(mAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("userName").value.toString()
                    val uniName = snapshot.child("university").value.toString()
                    val profileImage = snapshot.child("profileImage").value.toString()
                    binding.etName.setText(name)
                    binding.etUniName.setText(uniName)
                    try {
                        Glide.with(this@UpdateProfileActivity)
                            .load(profileImage)
                            .placeholder(R.drawable.person_icon)
                            .centerCrop()
                            .into(binding.roundImageView)
                    } catch (e: Exception) {
                        Log.d("GLIDE_ERROR", "onDataChange: ${e.message}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}