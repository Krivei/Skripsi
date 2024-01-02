package com.example.personalrecordv4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.personalrecordv4.databinding.ActivityCreateWorkoutBinding
import com.example.personalrecordv4.viewmodel.ExerciseViewModel
import com.example.personalrecordv4.viewmodel.SplitViewModel
import com.example.personalrecordv4.viewmodel.TutorialViewModel
import com.example.personalrecordv4.viewmodel.WorkoutPlanViewModel

class AddWorkoutPlanActivity : AppCompatActivity(){


    private val workoutPlanViewModel : WorkoutPlanViewModel by viewModels()
    private val splitViewModel : SplitViewModel by viewModels()
    private val exerciseViewModel : ExerciseViewModel by viewModels()
    private val tutorialViewModel : TutorialViewModel by viewModels()
    private lateinit var binding : ActivityCreateWorkoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val templateUsage = intent.getStringExtra("Template")
        tutorialViewModel.getTutorial()
        var programName  = ""
        var rep  = ""
        var set = ""
        var workoutname  = ""
        var planId = ""
        var split : String? = null
        var splitNumber : Int = 0
        var exampleNumber : Int = 0
        binding.radio.setOnCheckedChangeListener { group, checkedId ->
            var index = binding.radio.indexOfChild(findViewById(checkedId))
            when (index) {
                0 -> {
                    workoutname = "Full Body"
                    split = "2"
                    splitNumber = 2
                    exampleNumber = 1
                }
                1 -> {
                    workoutname = "Push-Pull-Legs"
                    split = "3"
                    splitNumber = 3
                    exampleNumber = 3
                }
                2 -> {
                    workoutname = "Upper-Lower"
                    split = "2"
                    splitNumber = 2
                    exampleNumber = 2
                }
            }
        }

        binding.btnaddworkout.setOnClickListener {
            val radio = binding.radio.checkedRadioButtonId
             programName = binding.etProgramInput.text.toString().trim()
             rep = binding.setRepetition.text.toString().trim()
             set = binding.setTarget.text.toString().trim()
            if (!workoutPlanViewModel.isNameValid(programName)){
                binding.etProgramInput.error = "Please Name Your Program"
                return@setOnClickListener
            }
            if (!workoutPlanViewModel.isRadioChecked(radio)){
                binding.btnaddworkout.error = "Please Pick a Type"
                return@setOnClickListener
            }
            if (!workoutPlanViewModel.isNameValid(rep)){
                binding.setRepetition.error = "Please Type a Valid Number"
                return@setOnClickListener
            } else if (!workoutPlanViewModel.isInputValid(rep.toInt())){
                binding.setRepetition.error = "Must be greater than 0"
                return@setOnClickListener
            }
            if (!workoutPlanViewModel.isNameValid(set)){
                binding.setTarget.error = "Please Type a Valid Number"
                return@setOnClickListener
            } else if (!workoutPlanViewModel.isInputValid(set.toInt())){
                binding.setTarget.error = "Must be greater than 0"
                return@setOnClickListener
            }

            exerciseViewModel.checkExercise(rep.toInt(),set.toInt())
            if (templateUsage=="Yes"){
//                workoutPlanViewModel.createTemplate(splitNumber,workoutname,rep.toInt(),set.toInt(),programName)
                  workoutPlanViewModel.addWorkoutPlan(programName,workoutname,set.toInt(),rep.toInt())

            } else {
                workoutPlanViewModel.addWorkoutPlan(programName,workoutname,set.toInt(),rep.toInt())
            }
        }
        binding.btnAddWorkoutBack.setOnClickListener {
            finish()
        }
        val intent = Intent(this, EditSplitActivity::class.java)

        workoutPlanViewModel.newWorkoutPlanId.observe(this@AddWorkoutPlanActivity) {
            when(templateUsage){
                "Yes"->{
                    if (it!=null){
                        splitViewModel.assignSplit(workoutPlanViewModel.newWorkoutPlanId.value.toString(),splitNumber)
                        exerciseViewModel.loopNumber.postValue(1)

                    }
                }
                "No"->{
                    if (it == null){
                        Log.i("AddWorkoutPlanActivity", "newWorkoutPlanId null")
                    } else {
                        Log.i("AddWorkoutPlanActivity", "newWorkoutPlanId filled")
                        intent.putExtra(
                            "workoutPlanId",
                            workoutPlanViewModel.newWorkoutPlanId.value.toString()
                        )
                        splitViewModel.assignSplit(workoutPlanViewModel.newWorkoutPlanId.value.toString(),splitNumber)

                    }
                }
            }
        } //
        splitViewModel.newSplitList.observe(this@AddWorkoutPlanActivity){
            if (it == null || it.isEmpty()) {
                Log.i("AddWorkoutPlanActivity", "NewWorkoutPlanId Null : ${splitViewModel.newSplitList.value}")
            } else {
                if (templateUsage=="No"){
                    Log.i("AddWorkoutPlanActivity", splitViewModel.newSplitList.value.toString())
                    intent.putExtra("splitId",splitViewModel.newSplitList.value!!.toTypedArray())
                    intent.putExtra("workoutName", programName)
                    intent.putExtra("repetition", rep.toInt())
                    intent.putExtra("set", set.toInt())
                    startActivity(intent)
                    finish()
                } else {
                    var loop = exerciseViewModel.loopNumber.value
                    exerciseViewModel.loopNumber.observe(this@AddWorkoutPlanActivity){
                        if (it!=null){
                            loop = exerciseViewModel.loopNumber.value
                            Log.i("Loop","Loop ${loop} : WorkoutPlanId ${workoutPlanViewModel.newWorkoutPlanId.value!!} : loop Number ${exerciseViewModel.loopNumber.value!!}")
                            splitViewModel.makeSplitTemplate(workoutPlanViewModel.newWorkoutPlanId.value!!,exerciseViewModel.loopNumber.value!!,exampleNumber,rep.toInt(),set.toInt())
                        }
                    }
                    splitViewModel.templateList.observe(this@AddWorkoutPlanActivity){mutableList ->
                        if (!mutableList.isNullOrEmpty()){
                            Log.i("AddWorkoutPlanActivity", splitViewModel.templateList.value?.size.toString())
                            exerciseViewModel.assignTemplateExercise(mutableList,rep.toInt(),set.toInt(),
                                splitViewModel.newSplitList.value!![loop!! -1],
                                splitNumber,loop!!)
                        } else {
                            Log.i("AddWorkoutPlanActivity", "Empty List")

                        }
                    }
                    exerciseViewModel.isDone.observe(this@AddWorkoutPlanActivity){
                        if (it!=null && it == true){
                            finish()
                        }
                    }

                }
            }
        } //
//        splitViewModel.templateList.observe(this@AddWorkoutPlanActivity){mutableList ->
//                exerciseViewModel.assignTemplateExercise(splitViewModel.templateList.value!!,rep.toInt(),set.toInt())
//        }

    }
}