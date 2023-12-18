package com.example.afoodable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.afoodable.databinding.ActivityProfileSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileSettings : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSettingsBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            getUserDataFromFirebase(userId)
        } else {
            // Handle case where user ID is null
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }

        // Inside the save button click listener
        binding.saveEditProfileBtn.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val fullName = binding.editFullName.text.toString()
                val userName = binding.editUserName.text.toString()
                val phoneNumber = binding.editPhone.text.toString()
                val userAddress = binding.editAddress.text.toString()

                // Create a HashMap to store only the fields that need to be updated
                val updatedDataMap = hashMapOf<String, Any>(
                    "fullName" to fullName,
                    "userName" to userName,
                    "phone" to phoneNumber,
                    "userAddress" to userAddress
                )

                database = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                database.updateChildren(updatedDataMap)
                    .addOnSuccessListener {
                        // Success message
                        Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        // Failure message
                        Toast.makeText(this, "Failed to Save", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Handle case where user ID is null
                Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getUserDataFromFirebase(userId: String) {
        database = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)

                    // Display user information in EditText views
                    binding.editFullName.setText(userData?.fullName)
                    binding.editUserName.setText(userData?.userName)
                    binding.editPhone.setText(userData?.phone)
                    binding.editAddress.setText(userData?.userAddress)
                } else {
                    // Handle case where user data does not exist
                    Toast.makeText(this@ProfileSettings, "User data does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this@ProfileSettings, "Error fetching user data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
