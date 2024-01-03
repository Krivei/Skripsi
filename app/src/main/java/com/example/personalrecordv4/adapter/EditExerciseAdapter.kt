package com.example.personalrecordv4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R
import com.example.personalrecordv4.listener.onItemClickListener
import com.example.personalrecordv4.model.Exercise

class EditExerciseAdapter(private val exerciselist : MutableList<Exercise>, val itemClick: onItemClickListener): RecyclerView.Adapter<EditExerciseAdapter.EditExerciseViewHolder>() {
    inner class EditExerciseViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ExerciseTitle: TextView = itemView.findViewById(R.id.tvPlanName)
        val ExerciseType : TextView = itemView.findViewById(R.id.tvPlanType)
        val clDo = itemView.findViewById<ConstraintLayout>(R.id.clDo)
        val clDelete = itemView.findViewById<ConstraintLayout>(R.id.clDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return EditExerciseViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return exerciselist.size
    }

    override fun onBindViewHolder(holder: EditExerciseViewHolder, position: Int) {
        val currentItem = exerciselist[position]
        holder.ExerciseTitle.text = currentItem.name
        holder.ExerciseType.text = currentItem.muscleType.toString()
        holder.clDo.visibility = View.GONE

        holder.clDelete.setOnClickListener {
            itemClick.OnItemDelete(currentItem.name,currentItem.defaultReps,currentItem.defaultSets,"")
        }
    }
}