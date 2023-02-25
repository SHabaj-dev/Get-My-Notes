package com.sbz.getmynotes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sbz.getmynotes.R

class ItemAdapter(val context:Context,val items:ArrayList<String>):RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_custom,
                parent,false
            )
        )

    }

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
        val item=items.get(position)
        //holder.
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

    }
}