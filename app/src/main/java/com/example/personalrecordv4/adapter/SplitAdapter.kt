package com.example.personalrecordv4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R
import com.example.personalrecordv4.model.Split

class SplitAdapter(private val splitList: MutableList<Split>) : RecyclerView.Adapter<SplitAdapter.SplitViewHolder>() {
    inner class SplitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val SplitTitle: TextView = itemView.findViewById(
            R.id.splittitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.split_item, parent, false)
        return SplitViewHolder(itemView)
    }

    override fun getItemCount(): Int {
    return splitList.size
    }

    override fun onBindViewHolder(holder: SplitViewHolder, position: Int) {
        val currentItem = splitList[position]
        holder.SplitTitle.text = currentItem.name
        holder.itemView.setOnClickListener {

        }
    }
}