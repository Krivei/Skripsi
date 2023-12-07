package com.example.personalrecordv4.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R
import com.example.personalrecordv4.SplitListFragment
import com.example.personalrecordv4.model.WorkoutPlan
import com.example.personalrecordv4.viewmodel.SplitViewModel

class WorkoutPlanAdapter(private val workoutPlanList: MutableList<WorkoutPlan>) : RecyclerView.Adapter<WorkoutPlanAdapter.WorkoutPlanViewHolder>(){
    inner class WorkoutPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val WorkoutPlanTitle: TextView = itemView.findViewById(
            R.id.tvPlanName)
        val WorkoutPlanType: TextView = itemView.findViewById(
            R.id.tvPlanType
        )

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
        holder.WorkoutPlanTitle.text = currentItem.Name
        holder.WorkoutPlanType.text = currentItem.Type
        holder.itemView.setOnClickListener {
            Log.i("Test", "Clicked: $currentItem")
            var spltids: Array<String> = arrayOf()
            currentItem.SplitId.forEach {
                spltids += it
            }
            val args =  Bundle()
            args.putStringArray("splitID", spltids)
            val fragment = SplitListFragment()
            fragment.arguments = args
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}