package com.example.personalrecordv4.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.listener.onItemClickListener
import com.example.personalrecordv4.R
import com.example.personalrecordv4.model.WorkoutPlan

class WorkoutPlanAdapter(private val workoutPlanList: MutableList<WorkoutPlan>, val itemClick: onItemClickListener) : RecyclerView.Adapter<WorkoutPlanAdapter.WorkoutPlanViewHolder>(){
    inner class WorkoutPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val WorkoutPlanTitle: TextView = itemView.findViewById(
            R.id.tvPlanName)
        val WorkoutPlanType: TextView = itemView.findViewById(
            R.id.tvPlanType
        )
        val clDelete = itemView.findViewById<ConstraintLayout>(R.id.clDelete)
        val clDo = itemView.findViewById<ConstraintLayout>(R.id.clDo)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutPlanViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return WorkoutPlanViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return workoutPlanList.size
    }

    override fun onBindViewHolder(holder: WorkoutPlanViewHolder, position: Int) {
        val currentItem = workoutPlanList[position]
        holder.WorkoutPlanTitle.text = currentItem.name
        holder.WorkoutPlanType.text = currentItem.type
        holder.itemView.setOnClickListener {
            Log.i("Test", "Clicked: $currentItem")
            var spltids: Array<String> = arrayOf()
            if (currentItem.splitId.contains("")){
                Log.i("Split Error", "Split is Empty")
            } else {
                currentItem.splitId.forEach {
                    spltids += it
                }
                itemClick.OnItemSelect(currentItem.name,currentItem.reps,currentItem.sets,spltids)
            }
        }
        holder.clDelete.setOnClickListener {
            itemClick.OnItemDelete(currentItem.name,position,position,"")
        }


        holder.clDo.setOnClickListener {
            var splitids : Array<String> = arrayOf()
            currentItem.splitId.forEach {
                splitids+=it
            }
            itemClick.OnWorkoutStart(currentItem.name,currentItem.reps,currentItem.sets,splitids, currentItem.type)
        }
    }
}