package com.example.personalrecordv4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.WorkoutSplitAdapter
import com.example.personalrecordv4.listener.onItemClickListener

class BuildPlanAcitvity : AppCompatActivity(), onItemClickListener {
    private var recyclerView : RecyclerView? = null
    private var listdata : MutableList<String> = mutableListOf()
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                val value = it.data?.getStringExtra("newSplit")
                Log.i("value",value.toString())
                if (value != null) {
                    listdata.add(value)
                }
            }
        }
    override fun OnItemClick(position: Int) {
        super.OnItemClick(position)
        val intent = Intent(this, AddExerciseWorkPlan::class.java)
        intent.putExtra("split",(position+1).toString())
        getResult.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
        var workoutname = intent.getStringExtra("workoutName")
        var splitnumber = intent.getStringExtra("split")
        var repetition = intent.getStringExtra("repetition")
        var split = arrayOf<Char>()
    setContentView(R.layout.fragment_add_workout)
        val workoutnameTV = findViewById<View>(R.id.tvWorkoutPlanName) as TextView
        workoutnameTV.text = workoutname
        if (workoutname != null) {
            Log.i("workoutname",workoutname)
        }
        when (splitnumber) {
            "2" -> {split = arrayOf('1','2')}
            "3" -> {split = arrayOf('1','2','3')}
        }
    recyclerView = findViewById(R.id.rvWorkoutSplit)
    recyclerView?.setHasFixedSize(true)
    recyclerView?.layoutManager = LinearLayoutManager(this)
    recyclerView?.adapter = WorkoutSplitAdapter(split, listdata, this)
}




}