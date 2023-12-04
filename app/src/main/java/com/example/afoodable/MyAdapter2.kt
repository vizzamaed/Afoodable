package com.example.afoodable

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MyAdapter2 (private val context:Context,private val productList:List<DataClass>):
    RecyclerView.Adapter<MyViewHolder2>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder2 {
        val view: View =LayoutInflater.from(parent.context).inflate(R.layout.product_item,parent,false)
        return MyViewHolder2(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        Glide.with(context).load(productList[position].dataImage).into(holder.recImageProduct)
        holder.recProductName.text=productList[position].dataItemName
        holder.recProductPrice.text=productList[position].dataItemPrice
        holder.recProductDescription.text=productList[position].dataItemDescription

    }


}

class MyViewHolder2(itemView: View): RecyclerView.ViewHolder(itemView){
    var recImageProduct: ImageView
    var recProductName: TextView
    var recProductPrice: TextView
    var recProductDescription: TextView
    var recCardProduct:CardView

    init {
        recImageProduct=itemView.findViewById(R.id.recImageProduct)
        recProductName=itemView.findViewById(R.id.recProductName)
        recProductPrice=itemView.findViewById(R.id.recProductPrice)
        recProductDescription=itemView.findViewById(R.id.recProductDescription)
        recCardProduct=itemView.findViewById(R.id.recCardProduct)
    }
}