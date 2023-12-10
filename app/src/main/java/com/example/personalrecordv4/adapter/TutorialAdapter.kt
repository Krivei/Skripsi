package com.example.personalrecordv4.adapter

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.R
import com.example.personalrecordv4.TutorialDetailFragment
import com.example.personalrecordv4.model.Exercise

class TutorialAdapter(private val tutorialList: MutableList<Exercise>) : RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder>() {

 inner class TutorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val MuscleType: TextView = itemView.findViewById(R.id.tvMuscleType)
        val ExerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tutorial_item, parent, false)
        return TutorialViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tutorialList.size
    }

    override fun onBindViewHolder(holder: TutorialViewHolder, position: Int) {
        val currentItem = tutorialList[position]
        holder.ExerciseName.text = currentItem.Name
        holder.MuscleType.text = currentItem.MuscleType.toString()
//        holder.Instruksi.text = currentItem.Instruction.joinToString("\n")
//        holder.btnTutorial.text = ("Try ${currentItem.Name}")
//        holder.TutorialVid.setVideoURI(Uri.parse(currentItem.TutorialVid))
//        holder.TutorialVid.setOnPreparedListener {
//            it.setLooping(true)
//            it.start()
//        }
        holder.itemView.setOnClickListener {
            val args = Bundle()
            args.putString("exercise", currentItem.Name)
            val fragment = TutorialDetailFragment()
            fragment.arguments = args
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }
}