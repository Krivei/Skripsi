package com.example.personalrecordv4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.personalrecordv4.R
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.model.WorkoutPlan

class WorkoutPlanAdapter():RecyclerView.Adapter<WorkoutPlanAdapter.ListViewHolder>(){
    private val _listPlan: MutableList<WorkoutPlan> = mutableListOf()
    inner class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNama: TextView = itemView.findViewById(R.id.tvPlanName)
        var tvTipe: TextView = itemView.findViewById(R.id.tvType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.plan_list, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return _listPlan.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.itemView.apply {
            holder.tvNama.text = "${_listPlan[position].name}"
            holder.tvTipe.text = "${_listPlan[position].Type}"
        }
    }

    fun updateListData(newPlans: MutableList<WorkoutPlan>){
        _listPlan.clear()
        _listPlan.addAll(newPlans)
    }

}