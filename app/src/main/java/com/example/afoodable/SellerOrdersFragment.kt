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
import com.example.afoodable.databinding.FragmentSellerOrdersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SellerOrdersFragment : Fragment() {

    private lateinit var binding: FragmentSellerOrdersBinding
    private lateinit var databaseReference: DatabaseReference

    private lateinit var dbref: DatabaseReference
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<ProductsData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellerOrdersBinding.inflate(inflater, container, false)
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
        val dbref = FirebaseDatabase.getInstance().getReference("Products")

        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productArrayList = ArrayList<ProductsData>()

                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.key // Get the seller's ID

                    userId?.let { sellerId ->
                        for (productSnapshot in userSnapshot.children) {
                            val productData = productSnapshot.getValue(ProductsData::class.java)
                            productData?.let {
                                productArrayList.add(it)
                            }
                        }
                    }
                }

                //
                productRecyclerView.adapter = SellerOrderAdapter(productArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }







}


