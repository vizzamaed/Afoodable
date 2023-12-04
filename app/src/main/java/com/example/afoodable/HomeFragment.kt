package com.example.afoodable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.afoodable.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var imagesList: ArrayList<ShareInfoImage>
    private lateinit var databaseReference: DatabaseReference

    private lateinit var productList: ArrayList<DataClass>
    private lateinit var adapter2: MyAdapter2
    var eventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    // Handle scenario where snapshot doesn't exist or contains no data
                    // Show a placeholder or handle empty state
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error (if any)
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        // Call Products() function when the view is created
        Products()
    }

    private fun Products() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerViewProduct.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        productList = ArrayList()
        adapter2 = MyAdapter2(requireContext(), productList)
        binding.recyclerViewProduct.adapter = adapter2
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Inventory")
        dialog.show()

        eventListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClass::class.java)
                    dataClass?.let {
                        productList.add(it)
                    }
                }
                adapter2.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })
    }

}


