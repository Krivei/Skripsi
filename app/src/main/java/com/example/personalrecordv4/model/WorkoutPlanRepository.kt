package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.personalrecordv4.viewmodel.SplitViewModel
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject

class WorkoutPlanRepository() {
    var _listWorkoutPlan: MutableLiveData<MutableList<WorkoutPlan>> = MutableLiveData(mutableListOf())
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var _newWorkoutPlanId: MutableLiveData<String> = MutableLiveData()
    var _singleWorkoutPlan : MutableLiveData<WorkoutPlan> = MutableLiveData()
    var _listWorkoutPlanId : MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    private val db = Firebase.firestore.collection("WorkoutPlan")
    private val user = Firebase.firestore.collection("user")

    fun getWorkoutPlan(workoutPlan: MutableList<String>) {
        isLoading.postValue(true)
        db.whereIn(FieldPath.documentId(), workoutPlan).orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener { documents, e ->
           val listWorkoutPlan = mutableListOf<WorkoutPlan>()
            val listWorkoutPlanId = mutableListOf<String>()
            if (documents != null) {
                for (document in documents) {
                    val workoutPlan = document.toObject<WorkoutPlan>()
                    listWorkoutPlan.add(workoutPlan)
                    listWorkoutPlanId.add(document.id)
                    Log.i("WorkoutPlanRepository", "$workoutPlan")
                }
                _listWorkoutPlanId.postValue(listWorkoutPlanId)
                _listWorkoutPlan.postValue(listWorkoutPlan)
                isLoading.postValue(false)
            }else {
                Log.i("WorkoutPlanRepository", "Failed to Listen")
            }
        }
    }
    fun getSingleWorkoutPlan(planId : String){
        db.document(planId).get().addOnSuccessListener { documents ->
            _singleWorkoutPlan.postValue(documents.toObject<WorkoutPlan>())
        }
    }
    fun addWorkoutPlan(name: String, type: String, sets: Int, reps: Int){
        var newWorkoutPlan = WorkoutPlan(name,type, mutableListOf(), Timestamp.now(), sets, reps)
        db.add(newWorkoutPlan).addOnSuccessListener { docref ->
            Log.i("WorkoutPlanRepository",docref.id)
            UserViewModel().updateList(docref.id)
            _newWorkoutPlanId.postValue(docref.id)
        }
    }

    fun deleteWorkoutPlan(planId: String){
        var splitId = mutableListOf<String>()
        db.document(planId).get().addOnSuccessListener {
            val x = it.toObject<WorkoutPlan>()
            if (x?.splitId?.isEmpty() == true){
                db.document(planId).delete()
                SplitViewModel().removeSplit(splitId)
            }
        }

    }

    fun createTemplate(i: Int,type: String,reps: Int,sets: Int, name: String){
        var newWorkoutPlan = WorkoutPlan(name,type, mutableListOf(), Timestamp.now(), sets, reps)
        db.add(newWorkoutPlan).addOnSuccessListener { docref ->
            Log.i("WorkoutPlanRepository",docref.id)
            UserViewModel().updateList(docref.id)
            var exampleNumber : Int
            when (type){
                "Full Body" -> {
                    exampleNumber = 1
                    SplitViewModel().makeSplitTemplate(docref.id,i,exampleNumber,reps,sets)
                }
                "Upper-Lower" -> {
                    exampleNumber = 2
                    SplitViewModel().makeSplitTemplate(docref.id,i,exampleNumber,reps,sets)
                }
                "Push-Pull-Legs" -> {
                    exampleNumber = 3
                    SplitViewModel().makeSplitTemplate(docref.id,i,exampleNumber,reps,sets)
                }
            }
        }
    }
}
