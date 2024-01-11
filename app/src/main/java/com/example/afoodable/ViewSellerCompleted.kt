package com.example.afoodable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.afoodable.databinding.ActivityViewSellerCompletedBinding
import com.example.afoodable.databinding.ActivityViewSellerPickUpBinding
import com.example.afoodable.databinding.ActivityViewSellerPrepareBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class ViewSellerCompleted : AppCompatActivity() {
    var imageURL = ""
    var productID: String = ""
    var orderID: String = ""
    var userID: String = ""
    //
    var userName: String=""
    var phone: String=""


    private lateinit var binding: ActivityViewSellerCompletedBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSellerCompletedBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        if (bundle != null) {
            binding.detailItemName.text = bundle.getString("Item Name")
            binding.detailItemPrice.text = bundle.getString("Price")
            binding.detailItemDescription.text = bundle.getString("Description")
            binding.detailBuyer.text = bundle.getString("userName")
            binding.detailPhone.text = bundle.getString("phone")

            imageURL = bundle.getString("Image")!!

            Glide.with(this).load(bundle.getString("Image")).into(binding.detailImage)

        }

    }
}