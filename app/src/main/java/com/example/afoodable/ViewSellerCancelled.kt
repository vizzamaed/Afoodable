package com.example.afoodable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.afoodable.databinding.ActivityViewSellerCancelledBinding
import com.example.afoodable.databinding.ActivityViewSellerCompletedBinding
import com.example.afoodable.databinding.ActivityViewSellerPickUpBinding
import com.example.afoodable.databinding.ActivityViewSellerPrepareBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class ViewSellerCancelled : AppCompatActivity() {
    var imageURL = ""
    var productID: String = ""
    var orderID: String = ""


    private lateinit var binding: ActivityViewSellerCancelledBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSellerCancelledBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        if (bundle != null) {
            binding.detailItemName.text = bundle.getString("Item Name")
            binding.detailItemPrice.text = bundle.getString("Price")
            binding.detailItemDescription.text = bundle.getString("Description")
            binding.detailItemBusinessLocation.text = bundle.getString("businessLocation")
            binding.detailItemBusinessName.text = bundle.getString("businessName")

            imageURL = bundle.getString("Image")!!

            Glide.with(this).load(bundle.getString("Image")).into(binding.detailImage)

        }

    }
}