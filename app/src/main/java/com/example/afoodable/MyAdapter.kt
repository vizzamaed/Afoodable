package com.example.afoodable

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MyAdapter (private val context:android.content.Context, private var dataList:List<DataClass>): RecyclerView.Adapter<MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View=LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].dataImage).into(holder.recImageItem)
        holder.recItemName.text=dataList[position].dataItemName
        holder.recItemDescription.text=dataList[position].dataItemDescription
        holder.recItemPrice.text=dataList[position].dataItemPrice

        holder.recCardItem.setOnClickListener{
            val intent=Intent(context, ItemDetailActivity::class.java)
            intent.putExtra("Image",dataList[holder.adapterPosition].dataImage)
            intent.putExtra("Item Name",dataList[holder.adapterPosition].dataItemName)
            intent.putExtra("Description",dataList[holder.adapterPosition].dataItemDescription)
            intent.putExtra("Price",dataList[holder.adapterPosition].dataItemPrice)
            context.startActivity(intent)
        }
    }

    fun searchDataList(searchList: List<DataClass>){
        dataList=searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var recImageItem: ImageView
    var recItemName: TextView
    var recItemDescription: TextView
    var recItemPrice: TextView
    var recCardItem: CardView

    init{
        recImageItem=itemView.findViewById(R.id.recImageItem)
        recItemName=itemView.findViewById(R.id.recItemName)
        recItemDescription=itemView.findViewById(R.id.recItemDescription)
        recItemPrice=itemView.findViewById(R.id.recItemPrice)
        recCardItem=itemView.findViewById(R.id.recCardItem)

    }
}

