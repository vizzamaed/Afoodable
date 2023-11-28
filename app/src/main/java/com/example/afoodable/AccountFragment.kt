package com.example.afoodable

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.afoodable.databinding.ActivityLogInBinding

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


        val sellerDashboard= view.findViewById<Button>(R.id.sellerDashboard)
        val edit_Profile_button= view.findViewById<Button>(R.id.edit_Profile_button)
        val beSellerBtn= view.findViewById<Button>(R.id.beSellerBtn)

        edit_Profile_button.setOnClickListener {
            startActivity(Intent(context, ProfileSettings::class.java))
        }


        sellerDashboard.setOnClickListener {
            startActivity(Intent(context, SellerDashboard::class.java))
        }

        beSellerBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val view=layoutInflater.inflate(R.layout.sellermode_alert,null)

            builder.setView(view)
            val dialog=builder.create()

            view.findViewById<Button>(R.id.beSellerCancelBtn).setOnClickListener {
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.BeSellerRegisterBtn).setOnClickListener {
                startActivity(Intent(requireContext(),SellerRegistration::class.java))
                dialog.dismiss()
            }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()

        }

    }
}
