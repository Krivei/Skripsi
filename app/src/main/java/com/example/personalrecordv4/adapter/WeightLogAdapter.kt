package com.example.personalrecordv4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.personalrecordv4.R
import com.example.personalrecordv4.WeightLogFragment
import com.example.personalrecordv4.model.WeightLog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Calendar

class WeightLogAdapter(private val weightLogList: MutableList<WeightLog>) : RecyclerView.Adapter<WeightLogAdapter.WeightLogViewHolder>() {

    private val storageReference: StorageReference = FirebaseStorage.getInstance().getReference("skripsidb-af74b")
    inner class WeightLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val WeightLogDesc: TextView = itemView.findViewById(R.id.tvBerat)
        val WeightLogPict: ImageView = itemView.findViewById(R.id.ivBerat)
        val WeightLogDate : TextView = itemView.findViewById(R.id.tvTanggal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightLogViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.weightlog_item, parent, false)
        return WeightLogViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return weightLogList.size
    }

    override fun onBindViewHolder(holder: WeightLogViewHolder, position: Int) {
        val currentItem = weightLogList[position]
        Glide.with(holder.itemView.context).load(currentItem.URL).into(holder.WeightLogPict)
        holder.WeightLogDesc.text = "${currentItem.Weight.toString()} Kg"
        val calendar= Calendar.getInstance()
        calendar.time = currentItem.Created_at
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        holder.WeightLogDate.text = "${day}-${month}-${year}"
    }
}