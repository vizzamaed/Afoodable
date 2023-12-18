package com.example.afoodable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.afoodable.databinding.ActivityUpdateSellerProductsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateSellerProducts : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateSellerProductsBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateSellerProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemName = intent.getStringExtra("itemName")
        val itemPrice = intent.getStringExtra("itemPrice")
        val itemDescription = intent.getStringExtra("itemDescription")

        binding.referenceItemName.setText(itemName)
        binding.referenceItemName.isEnabled = false // Disable editing of itemName

        binding.editItemPrice.setText(itemPrice)
        binding.editItemDescription.setText(itemDescription)

        binding.editUpdateItemBtn.setOnClickListener {
            val referenceItemName = binding.referenceItemName.text.toString()
            val editItemDescription = binding.editItemDescription.text.toString()
            val editItemPrice = binding.editItemPrice.text.toString()

            updateData(referenceItemName, editItemDescription, editItemPrice)
        }
    }

    private fun updateData(itemName: String, itemDescription: String, itemPrice: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        uid?.let { userUid ->
            databaseReference =
                FirebaseDatabase.getInstance().getReference("Users").child(userUid)
                    .child("zsellerData").child("Inventory")
            val dataClass = mapOf(
                "dataItemDescription" to itemDescription,
                "dataItemPrice" to itemPrice
            )

            databaseReference.child(itemName).setValue(dataClass)
                .addOnSuccessListener {
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Unable to Update", Toast.LENGTH_SHORT).show()
                }
        }

        // Update in 'Products' similarly if needed
    }
}
