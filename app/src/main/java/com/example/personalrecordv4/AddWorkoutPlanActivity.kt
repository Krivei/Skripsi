package com.example.personalrecordv4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class AddWorkoutPlanActivity : AppCompatActivity(){


    private val REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_create_workout)
        val btnAddWorkout = findViewById<View>(R.id.btnaddworkout) as Button
        val setRepetition = findViewById<View>(R.id.setRepertition) as EditText
        val textview = findViewById<View>(R.id.textView21) as TextView
        val textview3 = findViewById<View>(R.id.textView19) as TextView
        val textview4 = findViewById<View>(R.id.repetition) as TextView
        val textview5 = findViewById<View>(R.id.textView23) as TextView
        val setSet = findViewById<View>(R.id.setTarget) as EditText
        val radioGroup = findViewById<RadioGroup>(R.id.radio)
        var workoutname : String? = null
        var split : String? = null
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
           if (checkedId != -1) {
                     textview.visibility = View.VISIBLE
                       textview3.visibility = View.VISIBLE
                       textview4.visibility = View.VISIBLE
                       textview5.visibility = View.VISIBLE
                       setRepetition.visibility = View.VISIBLE
                       setSet.visibility = View.VISIBLE
               var index = radioGroup.indexOfChild(findViewById(checkedId))
        when (index) {
             0 ->{ workoutname = "Full Body"
                 split = "2"}
            1 -> {workoutname = "Push-Pull-Legs"
            split = "3"}
            2 -> {workoutname = "Upper-Lower"
            split = "3"}
           }}

        })
        setRepetition.addTextChangedListener(object : TextWatcher {
           override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
           override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
           override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                     btnAddWorkout.visibility = View.INVISIBLE
                } else {
                     btnAddWorkout.visibility = View.VISIBLE
                }
           }
        })
        setSet.addTextChangedListener(object : TextWatcher {
           override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
           override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
           override fun afterTextChanged(s: Editable) {
               btnAddWorkout.isEnabled = !s.toString().isEmpty()
           }
        })
        btnAddWorkout.setOnClickListener(View.OnClickListener {
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            val radioButton = findViewById<View>(checkedRadioButtonId) as Button
            val repetition = setRepetition.text.toString()
            val set = setSet.text.toString()
            val intent = Intent(this, BuildPlanAcitvity::class.java)
            intent.putExtra("workoutName", workoutname.toString())
            intent.putExtra("repetition", repetition)
            intent.putExtra("set", set)
            intent.putExtra("split", split)
            startActivity(intent)
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE ) {
            if (data != null) {
                if (data.hasExtra("myData1")) {
                    Toast.makeText(this, data.extras?.getString("myData1"),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}