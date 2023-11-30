package com.example.dti_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dti_admin.databinding.ActivityAddSellerAccountBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddSellerAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSellerAccountBinding
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddSellerAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addSellerAccountBtn.setOnClickListener{
            val businessName=binding.businessName.text.toString()
            val sellerFullName=binding.sellerFullName.text.toString()
            val sellerEmail=binding.sellerEmail.text.toString()
            val businessLocation=binding.businessLocation.text.toString()
            val tinNumber=binding.tinNumber.text.toString()
            val dtiNumber=binding.dtiNumber.text.toString()
            val bfarNumber=binding.bfarNumber.text.toString()

            databaseReference=FirebaseDatabase.getInstance().getReference("Sellers")
            val sellers=SellerData(businessName, sellerFullName, sellerEmail, businessLocation, tinNumber, dtiNumber, bfarNumber)
            databaseReference.child(businessName).setValue(sellers).addOnSuccessListener {
                binding.businessName.text.clear()
                binding.sellerFullName.text.clear()
                binding.sellerEmail.text.clear()
                binding.businessLocation.text.clear()
                binding.tinNumber.text.clear()
                binding.dtiNumber.text.clear()
                binding.bfarNumber.text.clear()

                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@AddSellerAccountActivity, AdminManageFragment::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
            }


        }
    }
}