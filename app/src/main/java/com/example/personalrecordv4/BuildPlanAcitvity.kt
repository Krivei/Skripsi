package com.example.personalrecordv4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.WorkoutSplitAdapter

class BuildPlanAcitvity : AppCompatActivity(){
    private var recyclerView : RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
        var workoutname = intent.getStringExtra("workoutName")
        var splitnumber = intent.getStringExtra("split")
        var repetition = intent.getStringExtra("repetition")
        var split = arrayOf('1','2')
    setContentView(R.layout.fragment_add_workout)
        when (splitnumber) {
            "2" -> {split = arrayOf('1','2')}
            "3" -> {split = arrayOf('1','2','3')}
        }
    recyclerView = findViewById(R.id.rvWorkoutSplit)
    recyclerView?.setHasFixedSize(true)
    recyclerView?.layoutManager = LinearLayoutManager(this)
    recyclerView?.adapter = WorkoutSplitAdapter(split)
}


}