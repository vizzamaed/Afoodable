package com.example.afoodable


import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afoodable.databinding.FragmentCartBinding
import com.example.afoodable.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var imagesList: ArrayList<ShareInfoImage>
    private lateinit var databaseReference: DatabaseReference

    private lateinit var dbref: DatabaseReference
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<CartData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val placeItemBtn = view.findViewById<Button>(R.id.placeItemBtn)

        placeItemBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val inflatedView = layoutInflater.inflate(R.layout.pickup_delivery_option, null)

            builder.setView(inflatedView)
            val dialog = builder.create()

            val btnPickUp = inflatedView.findViewById<Button>(R.id.btnPickUp)
            val btnDeliver = inflatedView.findViewById<Button>(R.id.btnDeliver)

            btnPickUp.setOnClickListener {
                dialog.dismiss()
            }
            btnDeliver.setOnClickListener {
                dialog.dismiss()
            }

            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }



        super.onViewCreated(view, savedInstanceState)

        productRecyclerView = binding.recyclerViewProduct
        productRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        productRecyclerView.setHasFixedSize(true)

        productArrayList = arrayListOf()
        getProductData()

        val recyclerViewPromo = binding.recyclerViewPromo
        recyclerViewPromo.layoutManager = LinearLayoutManager(requireContext())

        imagesList = arrayListOf()
        databaseReference = FirebaseDatabase.getInstance().getReference("AdminData").child("ImageFolder")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapShot in snapshot.children) {
                        val image = dataSnapShot.getValue(ShareInfoImage::class.java)
                        image?.let {
                            imagesList.add(it)
                        }
                    }
                    recyclerViewPromo.adapter = InfoAdapter(imagesList, requireContext())
                } else {
                  //
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getProductData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        val dbref = FirebaseDatabase.getInstance().getReference("Users")
            .child(userId)
            .child("Orders")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productArrayList = ArrayList<CartData>() // Create a list to hold products

                for (orderSnapshot in snapshot.children) { // Iterate through orders of the user
                    val itemName = orderSnapshot.child("ItemName").getValue(String::class.java)
                    val itemDescription = orderSnapshot.child("Description").getValue(String::class.java)
                    val itemPrice = orderSnapshot.child("Price").getValue(String::class.java)
                    val itemImage = orderSnapshot.child("Image").getValue(String::class.java)

                    itemName?.let { name ->
                        val cartData = CartData(name, itemDescription, itemPrice, itemImage)
                        productArrayList.add(cartData)
                    }
                }

                // Assuming CartAdapter accepts ArrayList<CartData> as a parameter
                productRecyclerView.adapter = CartAdapter(productArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }







}