package com.example.personalrecordv4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R
import com.example.personalrecordv4.model.HistoryDetail

class HistoryDetailAdapter (private val historyDetail : MutableList<HistoryDetail>) : RecyclerView.Adapter<HistoryDetailAdapter.HistoryDetailViewHolder>(){
    class HistoryDetailViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ExerciseName : TextView = itemView.findViewById(R.id.tvExerciseTitle)
        val ExerciseWeight : TextView = itemView.findViewById(R.id.tvExerciseWeight)
        val ExerciseRep : TextView = itemView.findViewById(R.id.tvExerciseRep)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetailViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_detail_item,parent,false)
        return HistoryDetailViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return historyDetail.size
    }
    override fun onBindViewHolder(holder: HistoryDetailViewHolder, position: Int) {
        val currentItem = historyDetail[position]
        holder.ExerciseName.text = currentItem.name
        holder.ExerciseWeight.text = currentItem.weight.joinToString("\n") {"$it Kg"}
        holder.ExerciseRep.text = currentItem.reps.joinToString("\n") {"$it Repetitions"}
    }
}