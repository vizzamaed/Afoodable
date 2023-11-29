package com.example.afoodableadmin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class AdminManageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_manage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val admin_view_seller = view.findViewById<Button>(R.id.admin_view_seller)
        val admin_upload_seller = view.findViewById<Button>(R.id.admin_upload_seller)
        val admin_update_seller = view.findViewById<Button>(R.id.admin_update_seller)
        val admin_delete_seller = view.findViewById<Button>(R.id.admin_delete_seller)

        admin_upload_seller.setOnClickListener {
            startActivity(Intent(context, AdminAddSellerAccount::class.java))
        }
    }
}
