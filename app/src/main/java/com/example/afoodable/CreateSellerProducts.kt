package com.example.afoodable

import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.afoodable.databinding.ActivityCreateSellerProductsBinding
import com.example.afoodable.databinding.ActivityProfileSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat

class CreateSellerProducts : AppCompatActivity() {

    private lateinit var binding: ActivityCreateSellerProductsBinding
    var imageURL: String? = null
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSellerProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.uploadImage.setImageURI(uri)
            } else {
                Toast.makeText(this@CreateSellerProducts, "No Image Selected", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.saveNewItemBtn.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val storageReference = FirebaseStorage.getInstance().reference.child("Inventory Images")
            .child(uri!!.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@CreateSellerProducts)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            uploadData()
            dialog.dismiss()
        }.addOnFailureListener {
            dialog.dismiss()
        }
    }

    private fun uploadData() {
        val itemName = binding.uploadItemName.text.toString()
        val itemDescription = binding.uploadItemDescription.text.toString()
        val itemPrice = binding.uploadItemPrice.text.toString()

        val dataClass = DataClass(itemName, itemDescription, itemPrice, imageURL)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        // Get businessName from user data in Realtime Database
        uid?.let { userUid ->
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userUid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val businessName = dataSnapshot.child("userData").child("zsellerData").value.toString()

                    // Save inventory data under businessName
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(userUid)
                        .child("zsellerData")
                        .child("Inventory")
                        .child(itemName) // Use itemName as a child node
                        .setValue(dataClass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@CreateSellerProducts,
                                    "Saved",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                this@CreateSellerProducts,
                                e.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle onCancelled
                }
            })
        }
    }
}