package com.example.personalrecordv4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R
import com.example.personalrecordv4.listener.onItemClickListener
import com.example.personalrecordv4.model.Split

class EditSplitAdapter (private val splitList: MutableList<Split>, private val splitid: MutableList<String>, val itemClick: onItemClickListener) : RecyclerView.Adapter<EditSplitAdapter.EditSplitViewHolder>(){
    inner class EditSplitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val SplitTitle: TextView = itemView.findViewById(
            R.id.splittitle
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditSplitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.split_item, parent, false)
        return EditSplitViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return splitList.size
    }

    override fun onBindViewHolder(holder: EditSplitViewHolder, position: Int) {
        val currentItem = splitList[position]
        val currentId = splitid[position]
        var exerciseIds: Array<String> = arrayOf()
        holder.SplitTitle.text = currentItem.name
        holder.itemView.setOnClickListener {
            if (currentItem.exerciseId.isEmpty() || currentItem.exerciseId.contains("")){

            } else {
                currentItem.exerciseId.forEach {
                    exerciseIds += it
                }

            }
            itemClick.OnWorkoutStart(currentId,0,0,exerciseIds,currentItem.name)
        }
    }
}