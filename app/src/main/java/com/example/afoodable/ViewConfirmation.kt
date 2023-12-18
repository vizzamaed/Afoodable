package com.example.afoodable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.afoodable.databinding.ActivityItemDetailBinding
import com.example.afoodable.databinding.ActivityViewCartBinding
import com.example.afoodable.databinding.ActivityViewConfirmationBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ViewConfirmation : AppCompatActivity() {
    var imageURL = ""
    var productID: String = ""
    //



    private lateinit var binding: ActivityViewConfirmationBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        if (bundle != null) {
            binding.detailItemName.text = bundle.getString("Item Name")
            binding.detailItemPrice.text = bundle.getString("Price")
            binding.detailItemDescription.text = bundle.getString("Description")
            binding.detailItemBusinessLocation.text = bundle.getString("businessLocation")
            binding.detailItemBusinessName.text = bundle.getString("businessName")
            //

            imageURL = bundle.getString("Image")!!
            Glide.with(this).load(bundle.getString("Image")).into(binding.detailImage)

        }

        // Inside onCreate method of ViewConfirmation activity
        val databaseReference = FirebaseDatabase.getInstance().reference
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""




        productID = intent.getStringExtra("ProductID") ?: ""
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser


        binding.cancelOrderBtn.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid ?: ""

            val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Orders")

            databaseReference.child(productID).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@ViewConfirmation, "Item Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@ViewConfirmation, "Failed to Delete Item", Toast.LENGTH_SHORT).show()
                }
        }

    }

}