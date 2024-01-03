package com.example.afoodable


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.compose.material3.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afoodable.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var imagesList: ArrayList<ShareInfoImage>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dbref: DatabaseReference
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<ProductsData>
    private lateinit var adapter: MyAdapter2
    private lateinit var searchView: SearchView
    private lateinit var originalList: ArrayList<ProductsData>
    //
    private var sortCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                //
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        //
        //
        val sortUpBtn: Button = binding.sortUpBtn
        sortUpBtn.setOnClickListener {
            sortCount++
            sortProductList()
        }
    }

    /////
    private fun sortProductList() {
        when (sortCount % 3) {
            0 -> {
                // Sort from lowest to highest
                productArrayList = ArrayList(originalList.sortedBy { it.dataItemPrice?.toFloatOrNull() ?: Float.MAX_VALUE })
            }
            1 -> {
                // Sort from highest to lowest
                productArrayList = ArrayList(originalList.sortedByDescending { it.dataItemPrice?.toFloatOrNull() ?: Float.MIN_VALUE })
            }
            else -> {
                // Back to original state (unsorted)
                productArrayList = ArrayList(originalList)
            }
        }
        adapter.updateList(productArrayList)
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
                originalList = ArrayList(productArrayList)
                adapter = MyAdapter2(productArrayList)
                productRecyclerView.adapter = adapter
                setUpSearchView()
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })


    }
    private fun setUpSearchView() {
        searchView = binding.search
        //


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    // If the search query is empty, display the original list
                    adapter.updateList(originalList)
                } else {
                    // Filter the list based on the search query
                    val filteredList = originalList.filter { product ->
                        product.dataItemName?.contains(newText, ignoreCase = true) == true
                    } as ArrayList<ProductsData>
                    adapter.updateList(filteredList)
                }
                return true
            }

        })
    }



}


