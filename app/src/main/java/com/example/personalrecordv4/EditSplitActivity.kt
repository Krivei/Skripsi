package com.example.personalrecordv4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.EditSplitAdapter
import com.example.personalrecordv4.databinding.ActivityEditSplitBinding
import com.example.personalrecordv4.viewmodel.ExerciseViewModel
import com.example.personalrecordv4.viewmodel.SplitViewModel
import com.example.personalrecordv4.viewmodel.WorkoutPlanViewModel
import com.example.personalrecordv4.listener.onItemClickListener

class EditSplitActivity : AppCompatActivity() , onItemClickListener {
    private var recyclerView : RecyclerView? = null
    private val splitViewModel : SplitViewModel by viewModels()
    private val workoutPlanViewModel : WorkoutPlanViewModel by viewModels()
    private val exerciseViewModel : ExerciseViewModel by viewModels()
    private lateinit var binding : ActivityEditSplitBinding
    private var workoutname = ""
    private var repetition: Int = 0
    private var set: Int = 0
    private var splitIdArray = mutableListOf<String>()

    override fun OnItemSelect(name: String, reps: Int, sets: Int, spltids: Array<String>) {
        super.OnItemSelect(name, reps, sets, spltids)
        val intent = Intent(this,EditExerciseActivity::class.java)
        intent.putExtra("exerciseId",spltids)
        intent.putExtra("repetition",repetition)
        intent.putExtra("set",set)
        intent.putExtra("splitId",name)
        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSplitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        workoutname = intent.getStringExtra("workoutName").toString()
        repetition = intent.getIntExtra("repetition",10)
        set = intent.getIntExtra("set",3)
        splitIdArray = intent.getStringArrayExtra("splitId")!!.toMutableList()
        binding.textView18.text = workoutname
        binding.tvSet.text = set.toString()
        binding.tvRep.text = repetition.toString()
        binding.ivWPBack.setOnClickListener {
            finish()
        }
        binding.ivWPDone.setOnClickListener{
            val intent  = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (splitIdArray.isEmpty()){
                Log.i("EditSplitActivity","Array Empty")
            } else {
                splitViewModel.getSplit(splitIdArray)
            }

        splitViewModel.splitData.observe(this@EditSplitActivity) {
            if (it == null){
                Log.i("EditSplitActivity","Failed Fetching Data")
            } else {

                    recyclerView = findViewById(R.id.rvEditSplit)
                    recyclerView?.setHasFixedSize(true)
                    recyclerView?.layoutManager = LinearLayoutManager(this)
                    recyclerView?.adapter = EditSplitAdapter(splitViewModel.splitData.value!!,splitIdArray,this)

            }
        }

    }
}