package com.example.personalrecordv4

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.AddExerciseAdapter
import com.example.personalrecordv4.model.Split
import com.example.personalrecordv4.viewmodel.ExerciseViewModel
import com.example.personalrecordv4.viewmodel.SplitViewModel
import com.example.personalrecordv4.viewmodel.TutorialViewModel


class AddExerciseWorkPlan : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var submit: ImageView? = null
    private val tutorialViewModel = TutorialViewModel()
    private val exerciseViewModel = ExerciseViewModel()
    private var listsid: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splitlist : MutableList<String> = mutableListOf()
        val split = intent.getStringExtra("split")
        setContentView(R.layout.fragment_add_exercise_split)
        val splitTv = findViewById<View>(R.id.tvSplitName) as TextView
        val btnAddExer = findViewById<View>(R.id.btnAddExerciseWork) as Button
        splitTv.text = "Split $split"
        tutorialViewModel.getTutorial()
        val builderSingle = AlertDialog.Builder(this)
        submit = findViewById<View>(R.id.add_exercise_split_done) as ImageView
        tutorialViewModel.listTutorial.observe(this) { it ->
            if (it == null) {
                println("Gagal")
            } else {
                builderSingle.setTitle("Select One Name: ")
                val arrayAdapter = ArrayAdapter<String>(
                    this,
                    android.R.layout.select_dialog_singlechoice
                )
                for (i in it.indices) {
                    arrayAdapter.add(it[i].name)
                }
                builderSingle.setNegativeButton(
                    "cancel",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                builderSingle.setAdapter(arrayAdapter,
                    DialogInterface.OnClickListener { dialog, which ->
                        val strName = arrayAdapter.getItem(which)
                        val builderInner = AlertDialog.Builder(this)
                        builderInner.setMessage(strName.toString())
                        splitlist.add(strName.toString())
                        builderInner.setPositiveButton(
                            "Ok",
                            DialogInterface.OnClickListener { dialog, which ->
                                recyclerView.apply {
                                    recyclerView = this@AddExerciseWorkPlan.findViewById(R.id.rvAddExercise)
                                    recyclerView?.layoutManager = LinearLayoutManager(this?.context)
                                    recyclerView?.setHasFixedSize(true)
                                    recyclerView?.adapter = AddExerciseAdapter(splitlist)}
                                dialog.dismiss()
                            })
                        builderInner.show()
                    })
            }

        btnAddExer.setOnClickListener {
            builderSingle.show()
        }
        }
        submit?.setOnClickListener {
            AddNewSplit(splitlist)

        }
    }

    fun AddNewSplit(list: MutableList<String>){
        exerciseViewModel.getExerciseIdByName(list)
        exerciseViewModel.listId.observe(this) { it ->
            if (it == null) {
                println("Gagal")
            } else {
                if (it.size == list.size){
                    SplitViewModel().addSplit(Split("Split coba ${intent.getStringExtra("split")}",it))
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }

    }
}