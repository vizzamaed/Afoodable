package com.example.afoodable

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class CartAdapter (private val productList: ArrayList<ProductsData>): RecyclerView.Adapter<CartAdapter.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.cart_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productList[position]

        Log.d("CartAdapter", "Position: $position")
        Log.d("CartAdapter", "ItemName: ${currentItem.dataItemName}")
        Log.d("CartAdapter", "ItemPrice: ${currentItem.dataItemPrice}")
        Glide.with(holder.productImage.context).load(currentItem.dataImage).into(holder.productImage)
        holder.productName.text = currentItem.dataItemName
        holder.productPrice.text = currentItem.dataItemPrice
        holder.productDescription.text = currentItem.dataItemDescription
        holder.businessName.text = currentItem.businessName
        holder.businessLocation.text = currentItem.businessLocation



        holder.recCardProduct.setOnClickListener {
            val intent = Intent(holder.itemView.context, ViewCart::class.java)
            intent.putExtra("ProductID", productList[holder.adapterPosition].productID)
            intent.putExtra("Image", productList[holder.adapterPosition].dataImage)
            intent.putExtra("Item Name", productList[holder.adapterPosition].dataItemName)
            intent.putExtra("Description", productList[holder.adapterPosition].dataItemDescription)
            intent.putExtra("Price", productList[holder.adapterPosition].dataItemPrice)
            intent.putExtra("businessName", productList[holder.adapterPosition].businessName)
            intent.putExtra("businessLocation", productList[holder.adapterPosition].businessLocation)
            intent.putExtra("sellerID", productList[holder.adapterPosition].sellerID)
            holder.itemView.context.startActivity(intent)
            Log.d("CartAdapter", "ProductID: ${productList[holder.adapterPosition].productID}")
            Log.d("CartAdapter", "SellerID: ${productList[holder.adapterPosition].sellerID}")

        }


    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val productImage:ImageView=itemView.findViewById(R.id.recImageProduct)
        val productName:TextView=itemView.findViewById(R.id.recProductName)
        val productPrice:TextView=itemView.findViewById(R.id.recProductPrice)
        val productDescription:TextView=itemView.findViewById(R.id.recProductDescription)
        //
        val businessName:TextView=itemView.findViewById(R.id.recBusinessName)
        val businessLocation:TextView=itemView.findViewById(R.id.recBusinessLocation)
        val recCardProduct: CardView = itemView.findViewById(R.id.recCardProduct)
    }
}