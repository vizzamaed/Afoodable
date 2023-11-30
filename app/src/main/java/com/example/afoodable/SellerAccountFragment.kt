package com.example.afoodable

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context

class SellerAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_account, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchAndDisplayUserData()
//
        val buyerDashboardBtn = view.findViewById<Button>(R.id.buyerDashboardBtn)

        buyerDashboardBtn.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }

    }

    private fun fetchAndDisplayUserData(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData = snapshot.getValue(UserData::class.java)

                        // Update TextViews with user data
                        view?.findViewById<TextView>(R.id.userNameTextView)?.text = userData?.userName ?: ""
                        view?.findViewById<TextView>(R.id.fullNameTextView)?.text = userData?.fullName ?: ""
                        view?.findViewById<TextView>(R.id.phoneTextView)?.text = userData?.phone ?: ""
                        view?.findViewById<TextView>(R.id.addressTextView)?.text = userData?.userAddress ?: ""
                    } else {
                        Log.d("AccountFragment", "Snapshot does not exist for user ID: $userId")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("AccountFragment", "Database error: ${error.message}")
                }
            })
        } else {
            Log.e("AccountFragment", "User not authenticated")
        }

    }



}