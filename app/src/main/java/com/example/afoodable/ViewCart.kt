package com.example.afoodable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.afoodable.databinding.ActivityItemDetailBinding
import com.example.afoodable.databinding.ActivityViewCartBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ViewCart : AppCompatActivity() {
    var imageURL = ""
    var productID: String = ""
    var sellerID: String = ""



    private lateinit var binding: ActivityViewCartBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCartBinding.inflate(layoutInflater)
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
        var itemPrice = binding.detailItemPrice.text.toString().toFloatOrNull() ?: 0.0f
        val totalPriceTextView = binding.totalPriceTextView

        val decreaseBtn = binding.decreaseBtn
        val increaseBtn = binding.increaseBtn
        val quantityTextView = binding.quantityTextView


        var count = quantityTextView.text.toString().toIntOrNull() ?: 1

        decreaseBtn.setOnClickListener {
            count--
            if (count >= 0) {
                quantityTextView.text = count.toString()
                totalPriceTextView.text = (count * itemPrice).toString()
            } else {
                count = 0
            }
        }

        increaseBtn.setOnClickListener {
            count++
            quantityTextView.text = count.toString()
            totalPriceTextView.text = (count * itemPrice).toString()
        }


        productID = intent.getStringExtra("ProductID") ?: ""
        sellerID=intent.getStringExtra("sellerID")?:""
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser


        binding.placeItemBtn.setOnClickListener {
            val itemName = binding.detailItemName.text.toString()
            val itemPrice = binding.detailItemPrice.text.toString()
            val itemDescription = binding.detailItemDescription.text.toString()
            val imageURL = imageURL // Assuming imageURL is a global variable
            val businessName = binding.detailItemBusinessName.text.toString()
            val businessLocation = binding.detailItemBusinessLocation.text.toString()
            val productID = /* Retrieve the product ID, passed from previous activity */

                currentUser?.let { user ->
                    val userId = user.uid
                    val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Orders")
                    val orderID = databaseReference.push().key ?: ""

                    val orderDetails = HashMap<String, Any>()
                    orderDetails["orderID"] = orderID
                    orderDetails["productID"] = productID//
                    orderDetails["ItemName"] = itemName
                    orderDetails["Description"] = itemDescription
                    orderDetails["Price"] = itemPrice
                    orderDetails["Image"] = imageURL
                    orderDetails["businessLocation"] = businessLocation
                    orderDetails["businessName"] = businessName
                    orderDetails["sellerID"] = sellerID

                    val itemPriceStr = binding.detailItemPrice.text.toString()
                    val itemPrice = itemPriceStr.toFloatOrNull() ?: 0.0f

                    val quantity = binding.quantityTextView.text.toString().toIntOrNull() ?: 1
                    val totalPrice = quantity * itemPrice
                    orderDetails["quantity"] = quantity
                    orderDetails["totalPrice"] = totalPrice


                    databaseReference.child(orderID).setValue(orderDetails)
                        .addOnSuccessListener {
                            saveToProducts(userId,user.uid, productID, orderDetails,sellerID)
                            Toast.makeText(this@ViewCart, "Item Placed", Toast.LENGTH_SHORT).show()
                            val cartReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Cart")
                            cartReference.child(productID).removeValue()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@ViewCart, "Failed to Place Item", Toast.LENGTH_SHORT).show()
                        }
                }
        }


        binding.placeDeleteBtn.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid ?: ""

            val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Cart")

            databaseReference.child(productID).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@ViewCart, "Item Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@ViewCart, "Failed to Delete Item", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun saveToProducts(
        userId: String,
        currentUserID: String,
        productID: String,
        orderDetails: HashMap<String, Any>,
        sellerID: String
    ) {
        val ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(sellerID)

        //

        orderDetails["userID"] = currentUserID

        ordersRef.child(orderDetails["orderID"] as String)
            .setValue(orderDetails)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Handle success
                } else {
                    // Handle failure
                }
            }
    }




}