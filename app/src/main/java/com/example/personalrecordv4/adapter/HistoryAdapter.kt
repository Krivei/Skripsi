package com.example.personalrecordv4.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.HistoryDetailActivity
import com.example.personalrecordv4.R
import com.example.personalrecordv4.listener.onItemClickListener
import com.example.personalrecordv4.model.History
import java.text.SimpleDateFormat

class HistoryAdapter(private val historyList : MutableList<History>, val itemClick: onItemClickListener) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val HistoryName : TextView = itemView.findViewById(R.id.tvNameHistory)
        val HistoryType : TextView = itemView.findViewById(R.id.tvHistoryType)
        val HistoryDate : TextView = itemView.findViewById(R.id.tvWorkoutDate)
        val HistorySplit : TextView = itemView.findViewById(R.id.tvSplitNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyList[position]
        val formattedDate = SimpleDateFormat("dd-MM-yyyy").format(currentItem.date.toDate())
        holder.HistoryDate.text = formattedDate
        holder.HistoryName.text = currentItem.name
        holder.HistoryType.text = currentItem.workoutType
        holder.HistorySplit.text = currentItem.split
        holder.itemView.setOnClickListener {
            itemClick.OnItemSelect(currentItem.name,0,0,currentItem.exerciseLogs.toTypedArray())
        }

    }
}