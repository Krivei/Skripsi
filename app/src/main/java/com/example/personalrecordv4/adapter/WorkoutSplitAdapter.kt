package com.example.personalrecordv4.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personalrecordv4.listener.onItemClickListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.impl.utils.ContextUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.AddExerciseWorkPlan
import com.example.personalrecordv4.R

class WorkoutSplitAdapter(splitnumber: Array<Char>,splitid: MutableList<String>, val itemClick: onItemClickListener) : RecyclerView.Adapter<WorkoutSplitAdapter.WorkoutSplitViewHolder>() {
    private val splitnumber = splitnumber
    private val splitid = splitid

    inner class WorkoutSplitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val WorkoutSplitTitle: TextView = itemView.findViewById(
            R.id.split_number)
        val WorkoutSplitId: TextView = itemView.findViewById(R.id.split_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutSplitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.workout_split_item, parent, false)
        return WorkoutSplitViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return splitnumber.size
    }

    override fun onBindViewHolder(holder: WorkoutSplitViewHolder, position: Int) {
        holder.WorkoutSplitTitle.text = "Split ${position+1}"
        if (splitid.size == 0){
            holder.WorkoutSplitId.text = "0"
        }else {
            holder.WorkoutSplitId.text = splitid[position]
        }
        holder.itemView.setOnClickListener {
              itemClick.OnItemClick(position)
        }
    }

}
