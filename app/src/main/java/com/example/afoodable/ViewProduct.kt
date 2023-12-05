package com.example.afoodable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.afoodable.databinding.ActivityItemDetailBinding
import com.example.afoodable.databinding.ActivityViewProductBinding
import com.google.firebase.database.DatabaseReference

class ViewProduct : AppCompatActivity() {
    var imageURL = ""


    private lateinit var binding: ActivityViewProductBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        if(bundle != null){
            binding.detailItemName.text=bundle.getString("Item Name")
            binding.detailItemPrice.text=bundle.getString("Price")
            binding.detailItemDescription.text=bundle.getString("Description")
            imageURL=bundle.getString("Image")!!
            Glide.with(this).load(bundle.getString("Image")).into(binding.detailImage)

        }


        binding.addToCartBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_delete_item, null)



            builder.setView(view)
            val dialog = builder.create()

            Toast.makeText(this@ViewProduct, "Item Added to Cart", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                finish()
        }

        binding.backBtn.setOnClickListener{
            finish()
        }
    }
}
