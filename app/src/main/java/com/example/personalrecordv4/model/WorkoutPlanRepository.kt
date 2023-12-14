package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject

class WorkoutPlanRepository() {
    var _listWorkoutPlan: MutableLiveData<MutableList<WorkoutPlan>> = MutableLiveData(mutableListOf())
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val db = Firebase.firestore.collection("WorkoutPlan")

    fun addWorkoutPlan(workoutPlan: WorkoutPlan) {
        db.document().set(workoutPlan).addOnSuccessListener { documentReference ->
//            Log.d("WorkoutPlan", "DocumentSnapshot added with ID: ${documentReference}")
//            Firebase.firestore.collection("user").document(auth!!).update("workoutPlanId", FieldValue.arrayUnion(documentReference))
//        }.addOnFailureListener { e ->
//            Log.w("WorkoutPlan", "Error adding document", e)
        }
    }

    fun getWorkoutPlan(workoutPlan: MutableList<String>) {
        isLoading.postValue(true)
        db.whereIn(FieldPath.documentId(), workoutPlan).get().addOnSuccessListener { documents ->
           val listWorkoutPlan = mutableListOf<WorkoutPlan>()
            for (document in documents) {
                val workoutPlan = document.toObject<WorkoutPlan>()
                listWorkoutPlan.add(workoutPlan)
                Log.i("WorkoutPlan", "$workoutPlan")
            }
            _listWorkoutPlan.postValue(listWorkoutPlan)
            isLoading.postValue(false)
        }.addOnFailureListener { exception ->
            Log.w("WorkOutPlan", "Error getting documents: ", exception)
        }
    }


}
