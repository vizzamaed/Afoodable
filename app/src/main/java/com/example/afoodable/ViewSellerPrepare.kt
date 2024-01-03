package com.example.afoodable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.afoodable.databinding.ActivityViewSellerPrepareBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class ViewSellerPrepare : AppCompatActivity() {
    var imageURL = ""
    var productID: String = ""
    var orderID: String = ""
    var userID: String = ""


    private lateinit var binding: ActivityViewSellerPrepareBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSellerPrepareBinding.inflate(layoutInflater)
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

        productID = intent.getStringExtra("ProductID") ?: ""
        orderID=intent.getStringExtra("orderID")?:""
        //
        userID=intent.getStringExtra("userID")?:""
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser


        binding.accept.setOnClickListener {
            val itemName = binding.detailItemName.text.toString()
            val itemPrice = binding.detailItemPrice.text.toString()
            val itemDescription = binding.detailItemDescription.text.toString()
            val imageURL = imageURL
            val businessName = binding.detailItemBusinessName.text.toString()
            val businessLocation = binding.detailItemBusinessLocation.text.toString()


            currentUser?.let { user ->
                val userId = user.uid
                val databaseReference = FirebaseDatabase.getInstance().getReference("Ready Orders").child(userId)
                val orderDetails = HashMap<String, Any>()
                orderDetails["ItemName"] = itemName
                orderDetails["Price"] = itemPrice
                orderDetails["Description"] = itemDescription
                orderDetails["Image"] = imageURL
                orderDetails["businessName"] = businessName
                orderDetails["businessLocation"] = businessLocation
                orderDetails["productID"] = productID
                orderDetails["orderID"] = orderID
                //
                orderDetails["userID"] = userID


                databaseReference.child(orderID).setValue(orderDetails)
                    .addOnSuccessListener {
                        Toast.makeText(this@ViewSellerPrepare, "Item Ready To PickUp/Deliver", Toast.LENGTH_SHORT).show()
                        val orderReference = FirebaseDatabase.getInstance().getReference("Preparing Orders").child(userId)
                        orderReference.child(orderID).removeValue()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@ViewSellerPrepare, "Failed to Place Item", Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }
}