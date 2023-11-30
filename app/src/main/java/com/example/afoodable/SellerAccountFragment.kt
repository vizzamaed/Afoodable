package com.example.afoodable

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.afoodable.databinding.FragmentSellerAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Context

class SellerAccountFragment : Fragment() {

    private lateinit var binding: FragmentSellerAccountBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_account, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
        binding.searchButton.setOnClickListener{
            val searchBusinessName:String=binding.searchBusinessName.text.toString()
            if(searchBusinessName.isNotEmpty()) {
                readData(searchBusinessName)
            }else{
                Toast.makeText(context, "Enter Business Name",Toast.LENGTH_SHORT).show()
            }
        }

        val buyerDashboardBtn = view.findViewById<Button>(R.id.buyerDashboardBtn)

        buyerDashboardBtn.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }

    }

    private fun readData(businessName: String) {
        databaseReference=FirebaseDatabase.getInstance().getReference("Sellers")
        databaseReference.child(businessName).get().addOnSuccessListener {
            if(it.exists()){
                val businessName=it.child("businessName").value
                val businessLocation=it.child("businessLocation").value
                Toast.makeText(context, "Result",Toast.LENGTH_SHORT).show()

                binding.searchBusinessName.text.clear()
                binding.accountBusinessName.text=businessName.toString()
                binding.accountBusinessLocation.text=businessLocation.toString()
            }else{
                Toast.makeText(context, "Does not exist",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Something went wrong",Toast.LENGTH_SHORT).show()
        }

    }


}