package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.activity.viewModels
import com.example.personalrecordv4.WorkoutPlanCallback
import com.example.personalrecordv4.WorkoutPlanResponse
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth

class WorkoutPlanRepository() {
    var _listWorkoutPlan: MutableLiveData<MutableList<WorkoutPlan>> = MutableLiveData(mutableListOf())
    var _listIdWorkoutPlan: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    var _spinnerAdapter: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    private val id = Firebase.auth.currentUser!!.uid
    private val db = Firebase.firestore.collection("user").document(id)

    init {
        updateData()
    }

    fun updateData(){

        db.addSnapshotListener{snapshot, e->
            if (e!=null){
                Log.w("TEST",e)
                return@addSnapshotListener
            }
            if (snapshot != null){
                val workoutPlanData = snapshot.data?.get("WorkoutplanId") as? Map<*, *>
                val listWorkoutPlan: MutableList<WorkoutPlan> = mutableListOf()
                val listIdWorkoutPlan: MutableList<String> = mutableListOf()
                val spinnerAdapter: MutableList<String> = mutableListOf()

                if (workoutPlanData != null) {
                    for (key in workoutPlanData.keys){
                        val workoutPlan = WorkoutPlan(key.toString(), workoutPlanData[key].toString())
                        listWorkoutPlan.add(workoutPlan)
                        spinnerAdapter.add(workoutPlan.name)
                    }

                    _listWorkoutPlan.value = listWorkoutPlan
                    _spinnerAdapter.value = spinnerAdapter
                }
            }
        }
    }
    fun get(callback: WorkoutPlanCallback){
        db.collection("user").whereEqualTo("id",id).get() .addOnSuccessListener { document ->
            if (document != null) {
                Log.d("A", "DocumentSnapshot data: ${document}")
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        db.get().addOnSuccessListener {
//            val response = WorkoutPlanResponse()
//            for ()
        }
    }
    fun addPlan(){}
}
