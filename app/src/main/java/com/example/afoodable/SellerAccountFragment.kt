package com.example.afoodable

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class SellerAccountFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seller_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val buyerDashboardBtn = view.findViewById<Button>(R.id.buyerDashboardBtn)

        buyerDashboardBtn.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }

        val logoutButton = view.findViewById<Button>(R.id.logoutBtn)

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

    }

    private fun fetchAndDisplayUserData(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userId).child("zsellerData")

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val SellerData = snapshot.getValue(SellerData::class.java)

                        view?.findViewById<TextView>(R.id.accountBusinessName)?.text = SellerData?.businessName ?: ""
                        view?.findViewById<TextView>(R.id.accountBusinessLocation)?.text = SellerData?.businessLocation ?: ""

                    } else {
                        Log.d("SellerAccountFragment", "Snapshot does not exist for user ID: $userId")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("SellerAccountFragment", "Database error: ${error.message}")
                }
            })
        } else {
            Log.e("SellerAccountFragment", "User not authenticated")
        }

    }

    }
