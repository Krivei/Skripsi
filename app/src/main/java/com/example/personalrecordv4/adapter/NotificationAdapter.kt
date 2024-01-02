package com.example.personalrecordv4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R

class NotificationAdapter (private val days: List<Int>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    inner class NotificationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val onlineDays: TextView = itemView.findViewById(R.id.tvDays)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notif_item,parent,false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: NotificationAdapter.NotificationViewHolder,
        position: Int
    ) {
        val currentItem = days[position]
        val dayName = when(currentItem){
            1 -> "Mon"
            2 -> "Tue"
            3 -> "Wed"
            4 -> "Thu"
            5 -> "Fri"
            6 -> "Sat"
            7 -> "Sun"
            else -> throw IllegalArgumentException("Invalid Day")
        }
        holder.onlineDays.text = dayName
    }

    override fun getItemCount(): Int {
        return days.size
    }

}