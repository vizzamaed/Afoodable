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
import com.example.afoodable.databinding.ActivityViewProductBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ViewCart : AppCompatActivity() {
    var imageURL = ""


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
            //
            binding.detailItemBusinessLocation.text = bundle.getString("businessLocation")
            binding.detailItemBusinessName.text = bundle.getString("businessName")
            imageURL = bundle.getString("Image")!!
            Glide.with(this).load(bundle.getString("Image")).into(binding.detailImage)




            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser

            binding.placeItemBtn.setOnClickListener {
                val itemName = binding.detailItemName.text.toString()
                val itemPrice = binding.detailItemPrice.text.toString()
                val itemDescription = binding.detailItemDescription.text.toString()
                val imageURL = imageURL
                //
                val businessName = binding.detailItemBusinessName.text.toString()
                val businessLocation = binding.detailItemBusinessLocation.text.toString()
                //
                val productID = intent.getStringExtra("Product ID")

                currentUser?.let { user ->
                    val userId = user.uid

                    val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Orders")

                    val orderDetails = HashMap<String, Any>()
                    orderDetails["ItemName"] = itemName
                    orderDetails["Price"] = itemPrice
                    orderDetails["Description"] = itemDescription
                    orderDetails["Image"] = imageURL
                    //
                    orderDetails["businessName"] = businessName
                    orderDetails["businessLocation"] = businessLocation

                    productID?.let { productId ->
                        val productRef = databaseReference.child(productId)
                        productRef.setValue(orderDetails)
                            .addOnSuccessListener {
                                Toast.makeText(this@ViewCart, "Item Added to Cart", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@ViewCart, "Failed to add item to cart", Toast.LENGTH_SHORT).show()
                            }
                    }
                }



                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.dialog_delete_item, null)
                builder.setView(view)
                val dialog = builder.create()

                Toast.makeText(this@ViewCart, "Item Added to Cart", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                finish()
            }



            binding.placeDeleteBtn.setOnClickListener {
                finish()
            }
        }
    }


}