package com.example.personalrecordv4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R

class InstructionAdapter (private val type: List<String>): RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>(){

    inner class InstructionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val btnFilter : Button = itemView.findViewById(R.id.btnFilter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.instructionfilter_item, parent, false)
        return InstructionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return type.size
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {

    }
}