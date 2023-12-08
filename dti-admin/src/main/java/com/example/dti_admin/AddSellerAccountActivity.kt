package com.example.dti_admin

import SellerData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dti_admin.databinding.ActivityAddSellerAccountBinding
import com.google.firebase.database.*

class AddSellerAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSellerAccountBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSellerAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addSellerAccountBtn.setOnClickListener {
            val businessName = binding.businessName.text.toString()
            val sellerFullName = binding.sellerFullName.text.toString()
            val sellerEmail = binding.sellerEmail.text.toString()
            val businessLocation = binding.businessLocation.text.toString()
            val tinNumber = binding.tinNumber.text.toString()
            val dtiNumber = binding.dtiNumber.text.toString()
            val bfarNumber = binding.bfarNumber.text.toString()

            val usersReference = FirebaseDatabase.getInstance().getReference("Users")


            if (sellerEmail.isNotEmpty()) {
                usersReference.orderByChild("email").equalTo(sellerEmail)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (userSnapshot in dataSnapshot.children) {
                                val userKey = userSnapshot.key


                                val userSellerDataReference = usersReference.child(userKey!!)
                                    .child("zsellerData").child("businessData")

                                val sellers = SellerData(
                                    businessName,
                                    sellerFullName,
                                    sellerEmail,
                                    businessLocation,
                                    tinNumber,
                                    dtiNumber,
                                    bfarNumber
                                )

                                if (sellers.isValid()) {

                                    userSellerDataReference.setValue(sellers)
                                        .addOnSuccessListener {
                                            binding.businessName.text.clear()
                                            binding.sellerFullName.text.clear()
                                            binding.sellerEmail.text.clear()
                                            binding.businessLocation.text.clear()
                                            binding.tinNumber.text.clear()
                                            binding.dtiNumber.text.clear()
                                            binding.bfarNumber.text.clear()

                                            Toast.makeText(
                                                this@AddSellerAccountActivity,
                                                "Saved",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val intent = Intent(
                                                this@AddSellerAccountActivity,
                                                AdminManageFragment::class.java
                                            )
                                            startActivity(intent)
                                            finish()
                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                this@AddSellerAccountActivity,
                                                "Failed",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }else {
                                    Toast.makeText(
                                        this@AddSellerAccountActivity,
                                        "Invalid input for tinNumber, dtiNumber, or bfarNumber",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                }
                            }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle any errors
                            Toast.makeText(
                                this@AddSellerAccountActivity,
                                "Error: ${databaseError.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            } else {

                Toast.makeText(
                    this@AddSellerAccountActivity,
                    "Seller email is empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
