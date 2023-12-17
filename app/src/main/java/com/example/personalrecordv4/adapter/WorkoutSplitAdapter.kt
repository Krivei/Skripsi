package com.example.personalrecordv4.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.AddExerciseSplitFragment
import com.example.personalrecordv4.R

class WorkoutSplitAdapter(splitnumber: Array<Char>) : RecyclerView.Adapter<WorkoutSplitAdapter.WorkoutSplitViewHolder>() {
    private val splitnumber = splitnumber

    inner class WorkoutSplitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val WorkoutSplitTitle: TextView = itemView.findViewById(
            R.id.split_number)
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
        holder.itemView.setOnClickListener {
            val args =  Bundle()
            args.putString("splitnumber", splitnumber[position+1].toString())
            val fragments = AddExerciseSplitFragment()
            fragments.arguments = args
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragments)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

}
