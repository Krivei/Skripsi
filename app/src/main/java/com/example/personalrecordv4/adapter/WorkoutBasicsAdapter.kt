package com.example.personalrecordv4.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.BasicsDetailFragment
import com.example.personalrecordv4.R
import com.example.personalrecordv4.model.Basics

class WorkoutBasicsAdapter (private val basicList: MutableList<Basics>) : RecyclerView.Adapter<WorkoutBasicsAdapter.WorkoutBasicsViewHolder>() {
    inner class WorkoutBasicsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Title: TextView = itemView.findViewById(R.id.splittitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutBasicsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.split_item,parent,false)
        return WorkoutBasicsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return basicList.size
    }

    override fun onBindViewHolder(holder: WorkoutBasicsViewHolder, position: Int) {
        val currentItem = basicList[position]
        holder.Title.text = currentItem.name
        holder.itemView.setOnClickListener {
            val args = Bundle()
            args.putString("basics",currentItem.name)
            val fragment = BasicsDetailFragment()
            fragment.arguments = args
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}