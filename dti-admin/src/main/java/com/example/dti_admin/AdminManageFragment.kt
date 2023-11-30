package com.example.dti_admin

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.dti_admin.databinding.ActivityAddSellerAccountBinding

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


        val admin_view_list= view.findViewById<Button>(R.id.admin_view_list)
        val admin_add_seller_account= view.findViewById<Button>(R.id.admin_add_seller_account)
        val admin_update_seller_account= view.findViewById<Button>(R.id.admin_update_seller_account)
        val admin_delete_seller_account= view.findViewById<Button>(R.id.admin_delete_seller_account)

        admin_add_seller_account.setOnClickListener {
            startActivity(Intent(context,AddSellerAccountActivity::class.java))

        }

    }
}
