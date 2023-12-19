package com.example.afoodable


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afoodable.databinding.FragmentHomeBinding
import com.example.afoodable.databinding.FragmentPreparingOrderBinding
import com.example.afoodable.databinding.FragmentSellerCompletedBinding
import com.example.afoodable.databinding.FragmentSellerFailedBinding
import com.example.afoodable.databinding.FragmentSellerOrdersBinding
import com.example.afoodable.databinding.FragmentSellerPickUpDeliveryBinding
import com.example.afoodable.databinding.FragmentSellerPreparingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SellerFailedFragment : Fragment() {

    private lateinit var binding: FragmentSellerFailedBinding
    private lateinit var databaseReference: DatabaseReference

    private lateinit var dbref: DatabaseReference
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<ProductsData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellerFailedBinding.inflate(inflater, container, false)
        return binding.root


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        productRecyclerView = binding.recyclerViewProduct
        productRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        productRecyclerView.setHasFixedSize(true)

        productArrayList = arrayListOf()
        getProductData()

        //
    }

    private fun getProductData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        val dbref = FirebaseDatabase.getInstance().getReference("Failed Orders")
            .child(userId)


        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productArrayList = ArrayList<ProductsData>()

                for (orderSnapshot in snapshot.children) {
                    val itemDescription = orderSnapshot.child("Description").getValue(String::class.java)
                    val itemImage = orderSnapshot.child("Image").getValue(String::class.java)
                    val itemName = orderSnapshot.child("ItemName").getValue(String::class.java)
                    val itemPrice = orderSnapshot.child("Price").getValue(String::class.java)
                    val businessLocation = orderSnapshot.child("businessLocation").getValue(String::class.java)
                    val businessName = orderSnapshot.child("businessName").getValue(String::class.java)
                    val productID = orderSnapshot.key //
                    val sellerID = orderSnapshot.child("sellerID").getValue(String::class.java) ?: ""
                    //
                    val orderID = orderSnapshot.child("orderID").getValue(String::class.java) ?: ""
                    val productsData = ProductsData(productID, itemName, itemDescription, itemPrice, itemImage, businessName, businessLocation, sellerID,orderID)
                    productArrayList.add(productsData)

                }

                productRecyclerView.adapter = SellerFailedAdapter(productArrayList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }




}


