package com.example.afoodable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.afoodable.databinding.ActivityProfileSettingsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileSettings : AppCompatActivity() {

    //add
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var binding:ActivityProfileSettingsBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveEditProfileBtn.setOnClickListener{

            val fullName=binding.editFullName.text.toString()
            val userName=binding.editUserName.text.toString()
            val phoneNumber=binding.editPhone.text.toString()
            val userAddress=binding.editAddress.text.toString()

            //ADD
            val id=databaseReference.push().key.toString()
            database=FirebaseDatabase.getInstance().getReference("Users")
            val UserData =UserData(fullName,userName,phoneNumber,userAddress,)
            database.child(id).setValue(UserData).addOnSuccessListener {

                binding.editFullName.text.clear()
                binding.editUserName.text.clear()
                binding.editPhone.text.clear()
                binding.editAddress.text.clear()


                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {

                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

        }
    }
}