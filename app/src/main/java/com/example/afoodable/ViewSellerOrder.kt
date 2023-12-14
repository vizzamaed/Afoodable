package com.example.afoodable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.afoodable.databinding.ActivityItemDetailBinding
import com.example.afoodable.databinding.ActivityViewProductBinding
import com.example.afoodable.databinding.ActivityViewSellerOrderBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class ViewSellerOrder : AppCompatActivity() {
    var imageURL = ""
    var productID: String = ""


    private lateinit var binding: ActivityViewSellerOrderBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSellerOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        if (bundle != null) {
            binding.detailItemName.text = bundle.getString("Item Name")
            binding.detailItemPrice.text = bundle.getString("Price")
            binding.detailItemDescription.text = bundle.getString("Description")
            binding.detailItemBusinessLocation.text = bundle.getString("businessLocation")
            binding.detailItemBusinessName.text = bundle.getString("businessName")

            imageURL = bundle.getString("Image")!!
            //
            Glide.with(this).load(bundle.getString("Image")).into(binding.detailImage)

        }

        productID = intent.getStringExtra("ProductID") ?: ""
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Inside addToCartBtn click listener in ViewProduct class
        binding.accept.setOnClickListener {
            val itemName = binding.detailItemName.text.toString()
            val itemPrice = binding.detailItemPrice.text.toString()
            val itemDescription = binding.detailItemDescription.text.toString()
            val imageURL = imageURL // Assuming imageURL is a global variable
            val businessName = binding.detailItemBusinessName.text.toString()
            val businessLocation = binding.detailItemBusinessLocation.text.toString()
            val productID = /* Retrieve the product ID, passed from previous activity */

                currentUser?.let { user ->
                    val userId = user.uid
                    val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Cart")
                    val orderDetails = HashMap<String, Any>()
                    orderDetails["ItemName"] = itemName
                    orderDetails["Price"] = itemPrice
                    orderDetails["Description"] = itemDescription
                    orderDetails["Image"] = imageURL
                    orderDetails["businessName"] = businessName
                    orderDetails["businessLocation"] = businessLocation
                    orderDetails["productID"] = productID// Add product ID to the order details

                    // Set the order details under the item name as the key
                    databaseReference.child(productID).setValue(orderDetails)
                        .addOnSuccessListener {
                            Toast.makeText(this@ViewSellerOrder, "Item Added to Cart", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@ViewSellerOrder, "Failed to add item to cart", Toast.LENGTH_SHORT).show()
                        }
                }
        }




        binding.reject.setOnClickListener {
            finish()
        }
    }
}