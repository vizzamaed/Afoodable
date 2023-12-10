package com.example.afoodable

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.afoodable.databinding.ActivityItemDetailBinding
import com.example.afoodable.databinding.FragmentSellerProductsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class ItemDetailActivity : AppCompatActivity() {

    var imageURL = ""


    private lateinit var binding: ActivityItemDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        if(bundle != null){
            binding.detailItemName.text=bundle.getString("Item Name")
            binding.detailItemPrice.text=bundle.getString("Price")
            binding.detailItemDescription.text=bundle.getString("Description")
            imageURL=bundle.getString("Image")!!
            Glide.with(this).load(bundle.getString("Image")).into(binding.detailImage)

        }
        binding.updateItemBtn.setOnClickListener{
            val intent= Intent(this@ItemDetailActivity, UpdateSellerProducts::class.java)
            startActivity(intent)
            finish()
        }
        binding.deleteItemBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_delete_item, null)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnDelete).setOnClickListener {
                val bundle = intent.extras
                val itemName = bundle?.getString("Item Name") ?: ""

                val currentUser = FirebaseAuth.getInstance().currentUser
                val uid = currentUser?.uid

                deleteToProducts()

                uid?.let { userUid ->
                    val databaseReference =
                        FirebaseDatabase.getInstance().getReference("Users").child(userUid)
                            .child("zsellerData").child("Inventory")

                    // Query the item by name and delete it from the Firebase database
                    databaseReference.orderByChild("dataItemName").equalTo(itemName)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (itemSnapshot in snapshot.children) {
                                    // Remove the item from the database
                                    itemSnapshot.ref.removeValue()
                                }
                                Toast.makeText(
                                    this@ItemDetailActivity,
                                    "Item deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                                finish()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@ItemDetailActivity,
                                    "Failed to delete item",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                            }
                        })
                }
            }


            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }



    }

    private fun deleteToProducts() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_delete_item, null)

        builder.setView(view)
        val dialog = builder.create()

        val bundle = intent.extras
        val itemName = bundle?.getString("Item Name") ?: ""

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid


        uid?.let { userUid ->
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("Products").child(userUid)

            // Query the item by name and delete it from the Firebase database
            databaseReference.orderByChild("dataItemName").equalTo(itemName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (itemSnapshot in snapshot.children) {
                            itemSnapshot.ref.removeValue()
                        }
                        Toast.makeText(
                            this@ItemDetailActivity,
                            "Item deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                        finish()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@ItemDetailActivity,
                            "Failed to delete item",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }
                })
        }

    }

}