package com.example.afoodable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class InfoAdapter(private val imagesList:ArrayList<ShareInfoImage>,
    private val context: Context):
    RecyclerView.Adapter<InfoAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val image:ImageView=itemView.findViewById(R.id.promoItemImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.promo_item,parent,false)
        return  ImageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context).load(imagesList[position].ImageURL).into(holder.image)
    }
}