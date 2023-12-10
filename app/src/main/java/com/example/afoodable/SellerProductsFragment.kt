package com.example.afoodable

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afoodable.databinding.FragmentSellerProductsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SellerProductsFragment : Fragment() {

    private lateinit var binding: FragmentSellerProductsBinding
    private lateinit var adapter: MyAdapter
    private lateinit var dataList: ArrayList<DataClass>
    var databaseReference:DatabaseReference?=null
    var eventListener:ValueEventListener?=null

    private lateinit var addItemBtn: FloatingActionButton
    private lateinit var itemRecyclerview: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSellerProductsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager=GridLayoutManager(requireContext(),1)
        binding.recyclerView.layoutManager=gridLayoutManager

        val builder=AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog=builder.create()
        dialog.show()

        dataList= ArrayList()
        adapter= MyAdapter(requireContext(), dataList)
        binding.recyclerView.adapter=adapter

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid


        if (userId != null) {
            databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userId).child("zsellerData").child("Inventory")
            dialog.show()

            eventListener=databaseReference!!.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    dataList.clear()
                    for (itemSnapshot in snapshot.children){
                        val dataClass=itemSnapshot.getValue(DataClass::class.java)
                        if(dataClass!=null){

                            dataList.add(dataClass)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    dialog.dismiss()
                }

            })
        }




        binding.addItemBtn.setOnClickListener {
            val intent = Intent(requireContext(), CreateSellerProducts::class.java)
            startActivity(intent)
        }

        binding.search.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }

        })

    }
    fun searchList(text:String){
        val searchList=ArrayList<DataClass>()
        for (dataClass in dataList){
            if(dataClass.dataItemName?.lowercase()?.contains(text.lowercase())==true){
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }
}

