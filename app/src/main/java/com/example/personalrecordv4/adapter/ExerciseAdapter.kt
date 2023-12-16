package com.example.personalrecordv4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R
import com.example.personalrecordv4.model.Exercise

class ExerciseAdapter(private val exerciseList: MutableList<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>(){
    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ExerciseTitle: TextView = itemView.findViewById(R.id.splittitle)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.split_item,parent,false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.ExerciseViewHolder, position: Int) {
        val currentItem = exerciseList[position]
        holder.ExerciseTitle.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}