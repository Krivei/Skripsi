package com.example.personalrecordv4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.EditExerciseAdapter
import com.example.personalrecordv4.databinding.ActivityEditExerciseBinding
import com.example.personalrecordv4.listener.onItemClickListener
import com.example.personalrecordv4.model.Exercise
import com.example.personalrecordv4.viewmodel.ExerciseViewModel
import com.example.personalrecordv4.viewmodel.HistoryViewModel
import com.example.personalrecordv4.viewmodel.SplitViewModel

class EditExerciseActivity : AppCompatActivity() ,onItemClickListener {
    private var recyclerView : RecyclerView? = null
    private lateinit var binding : ActivityEditExerciseBinding
    private var repetition: Int = 0
    private var set: Int = 0
    private var splitIds : String = ""
    private var status = ""
    private var historyId : String = "null"
    private var workoutType = ""
    private var workoutName = ""
    private var splitName = ""
    private val splitViewModel : SplitViewModel by viewModels()
    private val exerciseViewModel : ExerciseViewModel by viewModels()
    private val historyViewModel : HistoryViewModel by viewModels()
    override fun OnItemDelete(nama: String, reps: Int, sets: Int, splitId: String){
        super.OnItemDelete(nama,reps,sets, splitId)
        var konfirmasi = AlertDialog.Builder(this)
        konfirmasi.setTitle("Delete Exercise?")
        konfirmasi.setMessage(nama)
        konfirmasi.setNegativeButton("Cancel"){dialog, which -> dialog.dismiss()}
        konfirmasi.setPositiveButton("Confirm"){dialog,which->
            exerciseViewModel.removeExercise(nama,reps,sets,splitIds)
            dialog.dismiss()
            recyclerView?.adapter?.notifyDataSetChanged()
        }
        konfirmasi.show()
    }

    override fun OnExerciseDo(nama: String) {
        super.OnExerciseDo(nama)
        var konfirmasi = AlertDialog.Builder(this)
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        konfirmasi.setView(input)
        konfirmasi.setTitle("Starting Exercise")
        konfirmasi.setMessage("Please input the weight")
        konfirmasi.setPositiveButton("Start"){dialog, which ->
            val weightInput = input.text.toString()
            if (weightInput.isNotEmpty()){
                val weightInInt = weightInput.toInt()
                if (weightInInt<=0){
                    Toast.makeText(this,"Enter a valid number",Toast.LENGTH_SHORT).show()
                } else {
                   val intent = Intent(this,ExerciseActivity::class.java)
                    intent.putExtra("Name",nama)
                    intent.putExtra("Set",set)
                    intent.putExtra("Rep",repetition)
                    intent.putExtra("weight",weightInInt)
                    intent.putExtra("Status",status)
                    intent.putExtra("historyId",historyId)
                    startActivityForResult(intent,100)
                }
            } else {
                Toast.makeText(this,"Enter a number",Toast.LENGTH_SHORT).show()
            }
        }
        konfirmasi.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repetition = intent.getIntExtra("repetition",10)
        set = intent.getIntExtra("set",3)
        splitIds = intent.getStringExtra("splitId").toString()
        status = intent.getStringExtra("Status").toString()
        workoutType = intent.getStringExtra("workoutType").toString()
        workoutName = intent.getStringExtra("workoutName").toString()
        splitName = intent.getStringExtra("splitName").toString()
        Log.i("ExerciseActivity",historyId)
        if (status=="Exercise"){
            binding.btnAddExerciseWork.visibility = View.GONE
        }
        binding.ivEEDone.visibility = View.GONE
        binding.ivEEBack.setOnClickListener { if (status=="Exercise"){
            val konfirmasi = AlertDialog.Builder(this)
            konfirmasi.setTitle("Cancel Exercise?")
            konfirmasi.setPositiveButton("Yes"){dialog,which ->
                finish()
            }
            konfirmasi.setNegativeButton("No"){dialog,which ->
                dialog.dismiss()
            }
            konfirmasi.show()
        } else {
            finish()
        }
        }
        binding.ivEEDone.setOnClickListener {
            if (status=="Exercise"){
                val konfirmasi = AlertDialog.Builder(this)
                konfirmasi.setTitle("Confirm Exit")
                konfirmasi.setPositiveButton("Yes"){dialog,which ->
                    finish()
                }
                konfirmasi.setNegativeButton("No"){dialog,which ->
                    dialog.dismiss()
                }
                konfirmasi.show()
            } else {
                finish()
            }
        }
        splitViewModel.getSingleSplit(splitIds)
        splitViewModel.singleSplit.observe(this@EditExerciseActivity){
            if (it!=null){
                binding.ivWorkoutName.text = splitViewModel.singleSplit.value!!.name
                if (it.exerciseId.isNotEmpty()){
                    exerciseViewModel.getExercise(it.exerciseId)
                    Log.i("EditExerciseActivity", "it.exerciseId : ${it.exerciseId}")
                } else {
                    Log.i("EditExerciseActivity", "Emptied")
                    recyclerView = findViewById(R.id.rvAddExercise)
                    recyclerView?.visibility = View.GONE
                }
            }
        }
        exerciseViewModel.listExercise.observe(this@EditExerciseActivity){
            if (it!=null){
                if (it.isNotEmpty()){
                    recyclerView = findViewById(R.id.rvAddExercise)
                    recyclerView?.visibility = View.VISIBLE
                    recyclerView?.setHasFixedSize(true)
                    recyclerView?.layoutManager = LinearLayoutManager(this)
                    recyclerView?.adapter = EditExerciseAdapter(exerciseViewModel.listExercise.value!!,this, status)
                }
            }
        }
        exerciseViewModel.getExerciseByNumbers(repetition,set)
        var builderSingle = AlertDialog.Builder(this)
        exerciseViewModel.pickExercise.observe(this@EditExerciseActivity){
            if (it!=null){
                builderSingle.setTitle("Select an Exercise: ")
                val arrayAdapter = ArrayAdapter<String>(
                    this,android.R.layout.select_dialog_singlechoice
                )
                for (i in it.indices){
                    arrayAdapter.add(it[i].name)
                }
                builderSingle.setNegativeButton("cancel") {dialog, which -> dialog.dismiss()}
                builderSingle.setAdapter(arrayAdapter) {dialog, which ->
                    val strName = arrayAdapter.getItem(which)
                    exerciseViewModel.pickExercise(strName!!,repetition,set,splitIds)
                    val builderInner = AlertDialog.Builder(this)
                    builderInner.setTitle("Exercise Added!")
                    builderInner.setPositiveButton("Ok"){dialog, which ->
                        dialog.dismiss()
                    }
                    builderInner.show()
                }
                binding.btnAddExerciseWork.setOnClickListener {
                    builderSingle.show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100 && resultCode == RESULT_OK){
            historyId = data!!.getStringExtra("historyId").toString()
            val detailId = data!!.getStringExtra("detailId").toString()
            Log.i("ExerciseNih","history id $historyId")
            Log.i("ExerciseNih","detail id $detailId")
            if (historyId=="null"){
                if (detailId=="null"){
                } else {
                    binding.ivEEBack.visibility = View.GONE
                    binding.ivEEDone.visibility = View.VISIBLE
                    historyViewModel.createHistory(workoutName,workoutType,splitName,detailId)
                    historyViewModel.historyId.observe(this@EditExerciseActivity){
                        if (it!=null){
                            Log.i("ExerciseNih",it)
                            historyId = it
                            Log.i("ExerciseNih","history id 2 : $historyId")
                        }
                    }
                }
            } else {
                binding.ivEEBack.visibility = View.GONE
                binding.ivEEDone.visibility = View.VISIBLE
                historyViewModel.updateHistory(historyId,detailId)
            }
        }
    }
}