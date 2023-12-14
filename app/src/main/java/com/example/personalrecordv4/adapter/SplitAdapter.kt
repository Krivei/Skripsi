package com.example.personalrecordv4.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.ExerciseFragment
import com.example.personalrecordv4.R
import com.example.personalrecordv4.model.Split

class SplitAdapter(private val splitList: MutableList<Split>) : RecyclerView.Adapter<SplitAdapter.SplitViewHolder>() {
    inner class SplitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val SplitTitle: TextView = itemView.findViewById(
            R.id.splittitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.split_item, parent, false)
        return SplitViewHolder(itemView)
    }

    override fun getItemCount(): Int {
    return splitList.size
    }

    override fun onBindViewHolder(holder: SplitViewHolder, position: Int) {
        val currentItem = splitList[position]
        holder.SplitTitle.text = currentItem.name
        holder.itemView.setOnClickListener {
            var exerciseId: Array<String> = arrayOf()
            if (currentItem.exerciseId.contains("")){
                Log.i("SplitAdapter", "Contains Empty Exercise")
            } else {
                currentItem.exerciseId.forEach {
                    exerciseId+=it
                }
                val args = Bundle()
                args.putStringArray("exerciseId", exerciseId)
                val fragment = ExerciseFragment()
                fragment.arguments = args
                val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }
}