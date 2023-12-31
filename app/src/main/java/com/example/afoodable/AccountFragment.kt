package com.example.afoodable

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.afoodable.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchAndDisplayUserData()


        val edit_Profile_button= view.findViewById<Button>(R.id.edit_Profile_button)
        val beSellerBtn= view.findViewById<Button>(R.id.beSellerBtn)


        edit_Profile_button.setOnClickListener {
            startActivity(Intent(context, ProfileSettings::class.java))
        }


        val logoutButton = view.findViewById<Button>(R.id.logoutBtn)

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }




        beSellerBtn.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid

            if (userId != null) {
                val databaseReference = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(userId)
                    .child("zsellerData")
                    .child("businessData")

                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // businessData exists, navigate to seller dashboard
                            startActivity(Intent(requireContext(), SellerDashboard::class.java))
                        } else {
                            // businessData doesn't exist, show the sellermode_alert
                            val dialogView = layoutInflater.inflate(R.layout.sellermode_alert, null)
                            val builder = AlertDialog.Builder(requireContext())
                                .setView(dialogView)

                            val dialog = builder.create()
                            dialog.show()

                            // Handle the buttons inside the dialog
                            dialogView.findViewById<Button>(R.id.beSellerCancelBtn)?.setOnClickListener {
                                dialog.dismiss()
                            }

                            dialogView.findViewById<Button>(R.id.BeSellerRegisterBtn)?.setOnClickListener {
                                startActivity(Intent(requireContext(), SellerRegistration::class.java))
                                dialog.dismiss()
                            }
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

    private fun fetchAndDisplayUserData(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData = snapshot.getValue(UserData::class.java)

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
