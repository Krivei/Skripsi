package com.example.personalrecordv4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R

class AddExerciseAdapter(private val listExercise: List<String>): RecyclerView.Adapter<AddExerciseAdapter.AddExerciseViewHolder>() {
    inner class AddExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ExerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddExerciseAdapter.AddExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tutorial_item, parent, false)
        return AddExerciseViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listExercise.size
    }

    override fun onBindViewHolder(holder: AddExerciseViewHolder, position: Int) {
        val currentItem = listExercise[position]
        holder.ExerciseName.text = currentItem
    }
}