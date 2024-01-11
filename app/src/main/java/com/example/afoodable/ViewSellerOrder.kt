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
    var orderID: String = ""
    var userID: String=""
    //
    var userName: String=""
    var phone: String=""


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
            binding.detailBuyer.text = bundle.getString("userName")
            binding.detailPhone.text = bundle.getString("phone")

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
            val imageURL = imageURL // Assuming imageURL is a global variable
            val userName = binding.detailBuyer.text.toString()
            val phone = binding.detailPhone.text.toString()


            currentUser?.let { user ->
                val userId = user.uid
                    val databaseReference = FirebaseDatabase.getInstance().getReference("Preparing Orders").child(userId)
                    val orderDetails = HashMap<String, Any>()
                    orderDetails["ItemName"] = itemName
                    orderDetails["Price"] = itemPrice
                    orderDetails["Description"] = itemDescription
                    orderDetails["Image"] = imageURL
                    orderDetails["userName"] = userName
                    orderDetails["phone"] = phone
                    orderDetails["productID"] = productID
                    orderDetails["orderID"] = orderID
                    //
                    orderDetails["userID"] = userID



                    databaseReference.child(orderID).setValue(orderDetails)
                        .addOnSuccessListener {
                        Toast.makeText(this@ViewSellerOrder, "Preparing Item", Toast.LENGTH_SHORT).show()
                        val orderReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId)
                        orderReference.child(orderID).removeValue()
                        finish()
                    }
                        .addOnFailureListener {
                            Toast.makeText(this@ViewSellerOrder, "Failed to Place Item", Toast.LENGTH_SHORT).show()
                        }
                }
        }




        binding.reject.setOnClickListener {
            val itemName = binding.detailItemName.text.toString()
            val itemPrice = binding.detailItemPrice.text.toString()
            val itemDescription = binding.detailItemDescription.text.toString()
            val imageURL = imageURL // Assuming imageURL is a global variable
            val userName = binding.detailBuyer.text.toString()
            val phone = binding.detailPhone.text.toString()


            currentUser?.let { user ->
                val userId = user.uid
                val databaseReference = FirebaseDatabase.getInstance().getReference("Cancelled Orders").child(userId)
                val orderDetails = HashMap<String, Any>()
                orderDetails["ItemName"] = itemName
                orderDetails["Price"] = itemPrice
                orderDetails["Description"] = itemDescription
                orderDetails["Image"] = imageURL
                orderDetails["userName"] = userName
                orderDetails["phone"] = phone
                orderDetails["productID"] = productID
                orderDetails["orderID"] = orderID
                //
                orderDetails["userID"] = userID


                databaseReference.child(orderID).setValue(orderDetails)
                    .addOnSuccessListener {
                        Toast.makeText(this@ViewSellerOrder, "Successfully Cancelled Item", Toast.LENGTH_SHORT).show()
                        val orderReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId)
                        orderReference.child(orderID).removeValue()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@ViewSellerOrder, "Failed to Cancel Item", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}