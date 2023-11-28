package com.example.afoodable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.afoodable.databinding.ActivityUpdateSellerProductsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateSellerProducts : AppCompatActivity() {

    private lateinit var binding:ActivityUpdateSellerProductsBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateSellerProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editUpdateItemBtn.setOnClickListener {
            val referenceItemName=binding.referenceItemName.text.toString()
            val editItemDescription=binding.editItemDescription.text.toString()
            val editItemPrice=binding.editItemPrice.text.toString()


            updateData(referenceItemName,editItemDescription,editItemPrice)
        }
    }

    private fun updateData(itemName:String, itemDescription:String, itemPrice: String){
        databaseReference=FirebaseDatabase.getInstance().getReference("Inventory")
        val DataClass= mapOf<String, String>("dataItemDescription" to itemDescription,"dataItemPrice" to itemPrice)


        databaseReference.child(itemName).updateChildren(DataClass).addOnSuccessListener {
            binding.referenceItemName.text.clear()
            binding.editItemDescription.text.clear()
            binding.editItemPrice.text.clear()
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Unable to Update", Toast.LENGTH_SHORT).show()
        }

    }
}