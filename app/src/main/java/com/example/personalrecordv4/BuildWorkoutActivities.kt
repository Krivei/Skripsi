package com.example.personalrecordv4

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.WorkoutSplitAdapter

class BuildWorkoutActivities : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_workout)
        var workoutname = intent.getStringExtra("workoutName")
        var splitnumber = intent.getStringExtra("split")
        var repetition = intent.getStringExtra("repetition")
        var set = intent.getStringExtra("set")
        val tvWorkoutPlanName = findViewById<View>(R.id.tvWorkoutPlanName) as TextView
        val tvSplitNumber = findViewById<View>(R.id.tvSplitNumber) as TextView
        tvWorkoutPlanName.text = workoutname
        tvSplitNumber.text = splitnumber
        }
}